#include "skel.h"
#include "include/parser.h"
#include "utils.h"
#include "include/arpTable.h"
#include "queue.h"
#include <netinet/if_ether.h>
#include <net/if_arp.h>
#include <netinet/ip_icmp.h>
#include <netinet/ip.h>

#define MSGTYPE_FORWARD 0
#define MSGTYPE_ARP_REQUEST 1
#define MSGTYPE_ARP_REPLY 2
#define MSGTYPE_TTL_EXCEEDED 3
#define MSGTYPE_WRONG_CHECKSUM 4
#define MSGTYPE_ECHO_FOR_ME 5
#define MSGTYPE_DEST_UNREACH 6

queue q;
routeTable rTable;
arpTable arp_table;

void send_arp_request(int interface, struct in_addr address) {
    packet *m = calloc(1, sizeof(packet));
    struct ether_header *etherHeader = (struct ether_header *) m->payload;
    struct ether_arp *arpHeader = (struct ether_arp *) (m->payload + sizeof(struct ether_header));
    m->len = sizeof(struct ether_header) + sizeof(struct ether_arp);
    m->interface = interface;
    //destination - broadcast
    memset(etherHeader->ether_dhost, 0xff, 6);
    //source - interface mac
    get_interface_mac(interface, etherHeader->ether_shost);
    etherHeader->ether_type = htons(ETHERTYPE_ARP);

    //completing the ARP Header
    //hardware type - ethernet
    arpHeader->ea_hdr.ar_hrd = htons(ARPHRD_ETHER);
    arpHeader->ea_hdr.ar_pro = htons(ETHERTYPE_IP);
    //hardware length - 6 (MAC), protocol length - 4(IP)
    arpHeader->ea_hdr.ar_hln = 6;
    arpHeader->ea_hdr.ar_pln = 4;
    arpHeader->ea_hdr.ar_op = htons(ARPOP_REQUEST);
    get_interface_mac(interface, arpHeader->arp_sha);
    struct in_addr interfaceIP;
    inet_aton(get_interface_ip(interface), &interfaceIP);
    memcpy(arpHeader->arp_spa, &(interfaceIP.s_addr), 6);
    //target hardware address - broadcast, target
    memset(arpHeader->arp_tha, 0xff, 6);
    memcpy(arpHeader->arp_tpa, &(address.s_addr), 4);

    printf("SENDING ARP REQUEST!\n");
    send_packet(interface, m);

}



void forward(packet *m) {
    struct ether_header *eth_hdr = (struct ether_header *) m->payload;
    struct iphdr *ip_hdr = (struct iphdr *) (m->payload + sizeof(struct ether_header));
    routeTableEntry *next = getNextHop(ip_hdr->daddr, &rTable);
    if (next == NULL) {
        printf("[forward]COULDN'T FIND NEXT HOP in routing table\n");
        return;
    }
    uint16_t oldValue, newValue;
    memcpy(&oldValue, &(ip_hdr->ttl), 2);
    (ip_hdr->ttl)--;
    memcpy(&newValue, &(ip_hdr->ttl), 2);
    ip_hdr->check = rfc1624(ip_hdr->check, oldValue, newValue);
        // ip_hdr->check = 0;
        // ip_hdr->check = checksum(ip_hdr, sizeof(struct iphdr));

    struct arpEntry *arp = getArpEntry(next->next_hop);
    if (arp == NULL) {
        //make copy of packet
        packet *n = malloc(sizeof(packet));
        n->len = m->len;
        n->interface = m->interface;
        memcpy(n->payload, m->payload, m->len);
        queue_enq(q, n);
        send_arp_request(next->interface, next->next_hop);
        return;
    }
    get_interface_mac(next->interface, eth_hdr->ether_shost);
    memcpy(eth_hdr->ether_dhost, arp->mac, sizeof(arp->mac));
    printf("FORWARDING\n");
    send_packet(next->interface, m);
}

int getMessageType(packet *m) {
    struct ether_header *etherHeader = (struct ether_header *) m->payload;
    switch (htons(etherHeader->ether_type)) {
        case ETHERTYPE_IP: ;
            //IP
            struct iphdr *ip_header = (struct iphdr *) (m->payload + sizeof(struct ether_header));
            //now it will verify checksum
            __u16 oldCheck = ip_header->check;
            ip_header->check = 0;
            __u16 newCheck = checksum(ip_header, sizeof(struct iphdr));
            ip_header->check = oldCheck;
            if (oldCheck != newCheck) {
                printf("WRONG IP Checksum!\n");
                return MSGTYPE_WRONG_CHECKSUM;
            }
            if (ip_header->ttl <= 1) {
                return MSGTYPE_TTL_EXCEEDED;
            }
            if (getNextHop(ip_header->daddr, &rTable) == NULL) {
                //didn't find entry in rTable
                printf("NEXT HOP NOT FOUND!\n");
                return MSGTYPE_DEST_UNREACH;
            }
            if (1 == ip_header->protocol) {
                //ICMP packet
                struct icmphdr *icmp_header = (struct icmphdr *) (m->payload + sizeof(struct ether_header) +
                                                                  sizeof(struct iphdr));
                //verify ICMP checksum
                u_int16_t oldCheck = icmp_header->checksum;
                icmp_header->checksum = 0;
                icmp_header->checksum = checksum(icmp_header,
                                                 m->len - sizeof(struct ether_header) - sizeof(struct iphdr));
                if (oldCheck != icmp_header->checksum) {
                    printf("WRONG ICMP checksum!\n");
                    return MSGTYPE_WRONG_CHECKSUM;
                }
                struct in_addr interfaceIP;
                inet_aton(get_interface_ip(m->interface), &(interfaceIP));
                if (ip_header->daddr == interfaceIP.s_addr) {
                    if (icmp_header->type == ICMP_ECHO) {
                        return MSGTYPE_ECHO_FOR_ME;
                    }
                }
                return MSGTYPE_FORWARD;
            }
            return MSGTYPE_FORWARD;
            break;
        case ETHERTYPE_ARP: ;
            //ARP
            struct arphdr *arp_header = (struct arphdr *) (m->payload + sizeof(struct ether_header));
            switch (htons(arp_header->ar_op)) {
                case ARPOP_REQUEST:
                    //ARP REQUEST
                    return MSGTYPE_ARP_REQUEST;
                case ARPOP_REPLY:
                    //ARP REPLY
                    return MSGTYPE_ARP_REPLY;
                default:
                    printf("UNSUPPORTED ARP OPERATION - %us\n", arp_header->ar_op);
            }
            break;
        default:
            printf("UNKNOWN MESSAGE TYPE!\n");
            return -1;
    }
    printf("UNKNOWN ETHER TYPE - ");
    return -1;
}


void handle_arp_request(packet *m) {
    struct ether_header *eth_hdr = (struct ether_header *) m->payload;
    struct ether_arp *arp_header = (struct ether_arp *) (m->payload + sizeof(struct ether_header));
    m->len = sizeof(struct ether_header) + sizeof(struct ether_arp);
    //destination mac is source mac(we return the package)
    memcpy(eth_hdr->ether_dhost, eth_hdr->ether_shost, 6);
    //source mac is the mac of our interface
    get_interface_mac(m->interface, eth_hdr->ether_shost);

    //complete the ARP header
    eth_hdr->ether_type = htons(ETHERTYPE_ARP);
    arp_header->ea_hdr.ar_op = htons(ARPOP_REPLY);
    //Our target is the machine from which we got the packet
    //Destination IP and MAC -> former Target IP and MAC
    memcpy(arp_header->arp_tha, arp_header->arp_sha, 6);
    memcpy(arp_header->arp_tpa, arp_header->arp_spa, 4);
    //Sender IP and MAC - IP and MAC of our interface
    get_interface_mac(m->interface, arp_header->arp_sha);
    struct in_addr interfaceIP;
    inet_aton(get_interface_ip(m->interface), &interfaceIP);
    memcpy(arp_header->arp_spa, &(interfaceIP.s_addr), 4);

    printf("SENDING ARP REPLY!\n");
    send_packet(m->interface, m);

}

void handle_arp_reply(packet *m) {
    //update arp table
    //if there are pending packets for that address, send them
    printf("RECEIVED ARP REPLY!\n");
    struct ether_arp *arp_hdr = (struct ether_arp *) (m->payload + sizeof(struct ether_header));
    arpEntry *entry = newArpEntry();
    //copy IP and MAC from packet to entry
    memcpy(entry->mac, arp_hdr->arp_sha, 6);
    memcpy(&(entry->ip.s_addr), arp_hdr->arp_spa, 4);
    addArpEntry(entry);

    //check if there are pending packets for that address
    if (queue_empty(q)) {
        return;
    } else {
        while (!queue_empty(q)) {
            printf("FORWARDING PACKETS FROM QUEUE\n\n");
            forward((packet *) queue_deq(q));
        }
    }
}

void handle_echo(packet *m) {
    packet *n = calloc(1, sizeof(packet));
    n->len = m->len;
    n->interface = m->interface;
    memcpy(n->payload, m->payload, m->len);
    struct ether_header *eth_hdr = (struct ether_header *) n->payload;
    struct iphdr *ip_hdr = (struct iphdr *) (n->payload + IP_OFF);
    struct icmphdr *icmp_hdr = (struct icmphdr *) (n->payload + ICMP_OFF);
    //complete the ethernet header
    memcpy(eth_hdr->ether_dhost, eth_hdr->ether_shost, 6);
    get_interface_mac(n->interface, eth_hdr->ether_shost);

    //update ttl, addresses and checksum
    ip_hdr->ttl = 64;
    memcpy(&(ip_hdr->daddr), &(ip_hdr->saddr), 4);
    // set source IP
    struct in_addr interfaceIP;
    inet_aton(get_interface_ip(n->interface), &interfaceIP);
    memcpy(&(ip_hdr->saddr), &(interfaceIP.s_addr), 4);
    ip_hdr->check = 0;
    ip_hdr->check = checksum(ip_hdr, sizeof(struct iphdr));

    icmp_hdr->type = ICMP_ECHOREPLY;
    icmp_hdr->checksum = 0;
    icmp_hdr->checksum = checksum(icmp_hdr,
                                  m->len - sizeof(struct ether_header) - sizeof(struct iphdr));

    printf("SENDING ICMP ECHOREPLY!\n");
    send_packet(n->interface, n);

}

void send_icmp(packet *m, u_int8_t type) {
    struct ether_header *eth_hdr_m = (struct ether_header *) m->payload;
    struct iphdr *ip_hdr_m = (struct iphdr *) (m->payload + IP_OFF);
    struct icmphdr *icmp_hdr_m = (struct icmphdr *) (m->payload + ICMP_OFF);

    packet *n = malloc(sizeof(packet));
    struct ether_header *eth_hdr_n = (struct ether_header *) n->payload;
    struct iphdr *ip_hdr_n = (struct iphdr *) (n->payload + IP_OFF);
    struct icmphdr *icmp_hdr_n = (struct icmphdr *) (n->payload + ICMP_OFF);

    n->interface = m->interface;
    n->len = sizeof(struct ether_header) + sizeof(struct iphdr) + sizeof(struct icmphdr);
    //complete the ethernet header
    memcpy(eth_hdr_n->ether_dhost, eth_hdr_m->ether_shost, 6);
    memcpy(eth_hdr_n->ether_shost, eth_hdr_m->ether_dhost, 6);
    eth_hdr_n->ether_type = htons(ETHERTYPE_IP);
    //complete the IP header
    ip_hdr_n->protocol = 4;
    ip_hdr_n->ihl = 5;
    ip_hdr_n->version = 4;
    ip_hdr_n->tos = 0;
    ip_hdr_n->tot_len = htons(sizeof(struct iphdr) + sizeof(struct icmphdr));
    ip_hdr_n->id = getpid();
    ip_hdr_n->frag_off = 0;
    ip_hdr_n->ttl = 64;
    ip_hdr_n->tot_len = htons(sizeof(struct iphdr) + sizeof(struct icmphdr));
    // the protocol used is ICMP -> 1
    ip_hdr_n->protocol = 1;
    memcpy(&(ip_hdr_n->daddr), &(ip_hdr_m->saddr), 4);
    memcpy(&(ip_hdr_n->saddr), &(ip_hdr_m->daddr), 4);
    ip_hdr_n->check = 0;
    ip_hdr_n->check = checksum(ip_hdr_n, sizeof(struct iphdr));
    icmp_hdr_n->type = type;
    icmp_hdr_n->code = 0;
    icmp_hdr_n->un.echo.id = getpid();
    icmp_hdr_n->un.echo.sequence = icmp_hdr_m->un.echo.sequence;
    icmp_hdr_n->checksum = 0;
    icmp_hdr_n->checksum = checksum(icmp_hdr_n, sizeof(struct icmphdr));

    //interchange source and destination addresses
    memcpy(eth_hdr_n->ether_dhost, eth_hdr_m->ether_shost, 6);
    memcpy(eth_hdr_n->ether_shost, eth_hdr_m->ether_dhost, 6);
    send_packet(n->interface, n);
    printf("MESSAGE SENT\n");
}

int main(int argc, char *argv[]) {
    setvbuf(stdout, NULL, _IONBF, 0);

    parse_table(&rTable);
    qsort(rTable.entries, rTable.size, sizeof(routeTableEntry), comparator);

    packet m;
    int rc;
    q = queue_create();
    init();
    while (1) {
        rc = get_packet(&m);
        DIE(rc < 0, "get_message");
        /* Students will write code here */
        switch (getMessageType(&m)) {
            case MSGTYPE_FORWARD:
                //simple package - forward
                forward(&m);
                break;
            case MSGTYPE_ARP_REQUEST:
                //ARP Request for router
                handle_arp_request(&m);
                break;
            case MSGTYPE_ARP_REPLY:
                //ARP Reply
                //Update ARP table
                //Send pending packets for that router
                handle_arp_reply(&m);
                break;
            case MSGTYPE_TTL_EXCEEDED:
                //TTL < 1 or wrong address
                //Send ICMP message, drop packet
                send_icmp(&m, ICMP_TIME_EXCEEDED);
                break;
            case MSGTYPE_WRONG_CHECKSUM:
                //Wrong checksum
                //Drop packet
                break;
            case MSGTYPE_ECHO_FOR_ME:
                //ICMP ECHO for router
                handle_echo(&m);
                break;
            case MSGTYPE_DEST_UNREACH:
                //destination unreachable
                send_icmp(&m, ICMP_DEST_UNREACH);
                break;
            default:
                break;

        }
    }
}
