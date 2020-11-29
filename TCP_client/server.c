#include "server.h"
#include <math.h>
#include <sys/wait.h>
#include <errno.h>
//O lista globala de topicuri
//O lista globala de clienti

List topicList;
List clientList;

int rmrf(char *path) {
    DIR *theFolder = opendir(path);
    struct dirent *next_file;
    char filepath[300];

    while ((next_file = readdir(theFolder)) != NULL) {
        // build the path for each file in the folder
        sprintf(filepath, "%s/%s", "pending/", next_file->d_name);
        remove(filepath);
    }
    closedir(theFolder);
    remove(path);
    return 0;
}

Topic *getTopic(char *topicName) {
    List curr = topicList;
    while (curr) {
        Topic *t = (Topic *) curr->data;
        if (strncmp(topicName, t->name, strlen(topicName)) == 0) {
            return t;
        }
        curr = curr->next;
    }
    return NULL;
}

Subscriber* getSubscription(List l, Client *cl) {
    List curr = l;
    while (curr) {
        Subscriber *s = (Subscriber*)l->data;
        if (strcmp(s->cl->id, cl->id) == 0) {
            return s;
        }
        curr = curr->next;
    }
    return NULL;
}

void subscribe(Client *cl, char *topic, int sf) {
    //if topic doesn't exist - add
    //add subscriber to topic
    char message[100];
    Topic *t = getTopic(topic);
    if (!t) {
        //topic doesn't exist => needs to be created
        t = malloc(sizeof(Topic));
        strcpy(t->name, topic);
        topicList = addFirst(topicList, (void *) t);
    }
    //add subscriber to the existing topic
    Subscriber *s;
    if ((s = getSubscription(t->subscribers, cl)) != NULL) {
        //is already subscribed, update sf eventually
        s->SF = sf;
        sprintf(message, "Already subscribed!");
        sendMessage(cl, message);
        return;
    }
    s = malloc(sizeof(Subscriber));
    s->cl = cl;
    s->SF = sf;
    t->subscribers = addFirst(t->subscribers, (void *) s);
    sprintf(message, "Subscribed to %s", topic);
    sendMessage(cl, message);
}

void unsubscribe(Client *cl, char *topic) {
    //parcurgere a tuturor listelor topic->subscriber
    Topic *t = getTopic(topic);
    if (!t) {
        printf("Topic %s does not exist, cannot unsubscribe!\n", topic);
        return;
    }
    t->subscribers = deleteItem(t->subscribers, (void *) cl);
    char message[100];
    sprintf(message, "Unsubscribed from %s", topic);
    sendMessage(cl, message);

}

Client *getClientById(char *id) {
    List curr = clientList;
    Client *result;
    while (curr) {
        result = (Client *) curr->data;
        if (strcmp(result->id, id) == 0) {
            return result;
        }
        curr = curr->next;
    }
    return NULL;
}

Client *getClientBySock(int sockfd) {
    List curr = clientList;
    Client *result;
    while (curr) {
        result = (Client *) curr->data;
        if (result->socket == sockfd) {
            return result;
        }
        curr = curr->next;
    }
    return NULL;
}

//function that sends the message with given length to client c
void sendMessage(Client *c, char *message) {
    memset(buffer, 0, BUFLEN);
    int totLen = strlen(message)+1;
    memcpy(buffer, &totLen, sizeof(int));
    memcpy(buffer + sizeof(int), message, strlen(message));
    totLen += sizeof(int);
    send(c->socket, buffer, totLen, TCP_NODELAY);
}

void sendPendingMessages(Client *subscriber) {
    char filename[50] = {0};
    char buf[BUFLEN];
    strcat(filename, "pending/");
    strcat(filename, subscriber->id);
    FILE *f = fopen(filename, "r");
    if (!f) {
        //no pending messages
        return;
    }
    while (fgets(buf, BUFLEN, f)) {
        sendMessage(subscriber, buf);
    }

    fclose(f);
    remove(filename);
}

void addClientTCP(char *id, int length, int sockfd) {
    Client *newCl = getClientById(id);
    if (!newCl) {
        newCl = malloc(sizeof(Client));
        memcpy(newCl->id, id, length);
        newCl->active = 1;
        newCl->socket = sockfd;
        clientList = addFirst(clientList, newCl);
    } else {
        newCl->active = 1;
        newCl->socket = sockfd;
        sendPendingMessages(newCl);
    }
}

void addPendingMessage(Client *cl, char *message) {
    char filename[50] = {0};
    //mesajele pending vor fi tinute intr-un fisier cu id-ul userului din directorul pending
    strcat(filename, "pending/");
    strcat(filename, cl->id);
    FILE *f = fopen(filename, "a");
    if (!f) {
        printf("Could not open file!!!\n\n\n");
        printf("%s\n", filename);
        exit(1);
    }
    fprintf(f, "%s\n", message);
    fclose(f);
}

void distributeTopicUpdate(char *topic, char *message) {
    //verific si daca clientul e activ sau nu
    Topic *t = getTopic(topic);
    if (!t) {
        t = malloc(sizeof(Topic));
        strcpy(t->name, topic);
        topicList = addFirst(topicList, (void *) t);
        //daca abia am creat topicul, el nu are subscriberi
        return;
    }
    List subscriber = t->subscribers;
    while (subscriber) {
        Subscriber *s = (Subscriber *) subscriber->data;
        if (s->cl->active) {
            sendMessage(s->cl, message);
        } else if (s->SF) {
            addPendingMessage(s->cl, message);
        }
        subscriber = subscriber->next;
    }
}

void handleUdpMessage(char *message, struct sockaddr_in cli_addr) {
    char myMessage[BUFLEN] = {0};
    char *udpIP = inet_ntoa(cli_addr.sin_addr);
    char topic[MAX_TOPIC_LENGTH + 1];
    topic[MAX_TOPIC_LENGTH] = 0;
    memcpy(topic, message, MAX_TOPIC_LENGTH);
    char *typeID;
    char data[MAX_DATA_LENGTH + 1];
    switch (message[TYPE_START]) {
        case INT:
            typeID = "INT";
            int sign = (int) message[DATA_START];
            uint32_t value;
            memcpy(&value, message + DATA_START + 1, sizeof(uint32_t));
            value = ntohl(value);
            if (sign == 1) {
                value = -value;
            }
            sprintf(data, "%u", value);
            break;
        case SHORT_REAL:
            typeID = "SHORT_REAL";
            uint16_t abs;
            memcpy(&abs, message + DATA_START, sizeof(uint16_t));
            abs = ntohs(abs);
            float real = ((float) abs) / 100;
            sprintf(data, "%f", real);
            break;
        case FLOAT:
            typeID = "FLOAT";
            int sgn = (int) message[DATA_START];
            uint32_t val;
            uint8_t exp;
            memcpy(&val, message + DATA_START + 1, sizeof(uint32_t));
            memcpy(&exp, message + DATA_START + 1 + sizeof(uint32_t), sizeof(uint8_t));
            val = ntohl(val);
            float fl = powf(10, -exp) * val;
            if (sgn) {
                fl = -fl;
            }
            sprintf(data, "%f", fl);
            break;
        case STRING:
            typeID = "STRING";
            memcpy(data, message + DATA_START, MAX_DATA_LENGTH);
            break;
        default:
            return;
    }
    sprintf(myMessage, "%s:%hu - %s - %s - %s", udpIP, (ntohs(cli_addr.sin_port)), topic, typeID,
            data);
    distributeTopicUpdate(topic, myMessage);


}


void handleMessageTCP(Client *cl, char *message) {
    char *action = strtok(message, " ");
    char *topic = strtok(NULL, " ");
    char *sf = strtok(NULL, " ");
    char *errorMessage = "Bad request!";
    if (!topic) {
        printf("Invalid message!\n");
        sendMessage(cl, errorMessage);
        return;
    }
    if (strlen(topic) > MAX_TOPIC_LENGTH) {
        printf("Bad message! Topic name too long!\n");
        sendMessage(cl, errorMessage);
        return;
    }

    if (strcmp(action, "subscribe") == 0) {
        //Verific campul de sf
        if (!sf) {
            printf("Invalid message!\n");
            sendMessage(cl, errorMessage);
            return;
        }
        if (strcmp(sf, "1\n") == 0) {
            subscribe(cl, topic, 1);
            return;
        }
        if (strcmp(sf, "0\n") == 0) {
            subscribe(cl, topic, 0);
            return;
        }
        printf("Unknown SF value!\n");
        sendMessage(cl, errorMessage);
        return;
    } else {
        if (strcmp(action, "unsubscribe") != 0) {
            printf("Bad message! Invalid action!\n");
            sendMessage(cl, errorMessage);
            return;
        }
        unsubscribe(cl, topic);
    }


}

void freeClientList() {
    List curr = topicList;
    while (curr) {
        List next = curr->next;
        free((Client *) (curr->data));
        free(curr);
        curr = next;
    }
    free(curr);
}

void freeSubscriberList(List l) {

    while (l) {
        List next = l->next;
        free((Subscriber *) (l->data));
        free(l);
        l = next;
    }
    free(l);
}

void freeTopicList() {
    List curr = topicList;

    while (curr) {
        List next = curr->next;
        Topic *t = (Topic *) curr->data;
        if (!t) {
            return;
        }
        freeSubscriberList(t->subscribers);
        free(t);
        free(curr);
        curr = next;
    }
    free(curr);
}

void endSession(int fdMax) {
    //elibereaza toata memoria alocata
    //si toate fisierele cu mesaje pending
    rmrf("pending");
    for (int i = 0; i <= fdMax; ++i) {
        close(i);
    }
    freeClientList();
    freeTopicList();
}

void usage(char *file) {
    fprintf(stderr, "Usage: %s server_port\n", file);
    exit(0);
}

int max(int n1, int n2) {
    return (n1 > n2 ? n1 : n2);
}

int main(int argc, char *argv[]) {
    int sockfd_UDP, sockfd_TCP, newsockfd, portno;
    char buffer[BUFLEN];
    struct sockaddr_in serv_addr, cli_addr;
    int n, i, ret;
    socklen_t clilen;

    fd_set read_fds;    // multimea de citire folosita in select()
    fd_set tmp_fds;        // multime folosita temporar
    int fdmax;            // valoare maxima fd din multimea read_fds

    if (argc < 1) {
        usage(argv[0]);
    }

    //creez director pentru mesaje in asteptare
    mkdir("pending", 0700);

    // se goleste multimea de descriptori de citire (read_fds) si multimea temporara (tmp_fds)
    FD_ZERO(&read_fds);
    FD_ZERO(&tmp_fds);

    //creez un socket TCP pentru listen
    sockfd_TCP = socket(AF_INET, SOCK_STREAM, 0);
    int flags = -1;
    setsockopt(sockfd_TCP, IPPROTO_TCP, SO_REUSEADDR, (void *) &flags, sizeof(flags));
    setsockopt(sockfd_TCP, IPPROTO_TCP, SO_REUSEPORT, (void *) &flags, sizeof(flags));
    DIE(sockfd_TCP < 0, "socket TCP");
    //creez un socket UDP
    sockfd_UDP = socket(AF_INET, SOCK_DGRAM, 0);
    setsockopt(sockfd_UDP, IPPROTO_TCP, SO_REUSEADDR, (void *) &flags, sizeof(flags));
    setsockopt(sockfd_UDP, IPPROTO_TCP, SO_REUSEPORT, (void *) &flags, sizeof(flags));
    DIE(sockfd_TCP < 0, "socket UDP");

    portno = atoi(argv[1]);
    DIE(portno == 0, "atoi");

    memset((char *) &serv_addr, 0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(portno);
    serv_addr.sin_addr.s_addr = INADDR_ANY;

    //bind pentru socketul de TCP
    ret = bind(sockfd_TCP, (struct sockaddr *) &serv_addr, sizeof(struct sockaddr));
    DIE(ret < 0, "bind");
    ret = bind(sockfd_UDP, (struct sockaddr *) &serv_addr, sizeof(struct sockaddr));
    DIE(ret < 0, "bind");
    ret = listen(sockfd_TCP, MAX_CLIENTS);
    DIE(ret < 0, "listen");

    // se adauga noul file descriptor (socketul pe care se asculta conexiuni) in multimea read_fds
    //adaug si stdin la socketurile pe care ascult
    FD_SET(sockfd_TCP, &read_fds);
    FD_SET(sockfd_UDP, &read_fds);
    FD_SET(STDIN_FILENO, &read_fds);
    fdmax = max(sockfd_TCP, sockfd_UDP);
    while (1) {
        tmp_fds = read_fds;

        ret = select(fdmax + 1, &tmp_fds, NULL, NULL, NULL);
        DIE(ret < 0, "select");

        for (i = 0; i <= fdmax; i++) {
            if (FD_ISSET(i, &tmp_fds)) {
                memset(buffer, 0, BUFLEN);
                if (i == STDIN_FILENO) {
                    //mesaj din linia de comanda - verific daca e "exit"
                    fgets(buffer, BUFLEN, stdin);
                    if ((strcmp("exit\n", buffer) == 0)) {
                        printf("Received exit command!\n");
                        endSession(fdmax);
                        return 0;
                    } else {
                        printf("Received invalid command line message!\n");
                    }
                    continue;
                }
                if (i == sockfd_TCP) {
                    // a venit o cerere de conexiune pe socketul inactiv (cel cu listen),
                    // pe care serverul o accepta
                    clilen = sizeof(cli_addr);
                    newsockfd = accept(sockfd_TCP, (struct sockaddr *) &cli_addr, &clilen);
                    DIE(newsockfd < 0, "accept");
                    int opt = setsockopt(newsockfd, IPPROTO_TCP, TCP_NODELAY, (void *) &flags, sizeof(flags));
                    DIE(opt < 0, "Setting TCP_NODELAY");
                    // se adauga noul socket intors de accept() la multimea descriptorilor de citire
                    // se face update la file descriptorul maximal
                    FD_SET(newsockfd, &read_fds);
                    fdmax = max(newsockfd, fdmax);
                    memset(buffer, 0, BUFLEN);
                    n = recv(newsockfd, buffer, BUFLEN, 0);
                    if (n > MAX_ID_LENGTH) {
                        printf("Invalid ID! Too long!\n");
                        continue;
                    }
                    printf("New client %s connected from %s:%d.\n", buffer,inet_ntoa(cli_addr.sin_addr), (cli_addr.sin_port));
                    addClientTCP(buffer, n, newsockfd);
                    continue;
                }
                if (i == sockfd_UDP) {
                    //mesaj de la clientii UDP
                    memset(buffer, 0, BUFLEN);
                    clilen = sizeof(cli_addr);
                    int bytesRead = recvfrom(sockfd_UDP, buffer, BUFLEN, 0, (struct sockaddr *) &cli_addr,
                                             &clilen);
                    if (bytesRead > MAX_MESSAGE_LEN) {
                        printf("Too long message!\n");
                        continue;
                    }
                    handleUdpMessage(buffer, cli_addr);
                    continue;
                } else {
                    //mesaj de la unul dintre clientii TCP
                    memset(buffer, 0, BUFLEN);
                    n = recv(i, buffer, sizeof(buffer), 0);
                    Client *cl = getClientBySock(i);
                    if (n <= 0) {
                        // conexiunea s-a inchis
                        printf("Client %s disconnected.\n", cl->id);
                        //schimb statusul la passive
                        cl->active = 0;
                        close(i);
                        // se scoate din multimea de citire socketul inchis
                        FD_CLR(i, &read_fds);
                    } else {
                        handleMessageTCP(cl, buffer);
                    }
                }
            }
        }
    }

}

List deleteItem(List l, Client *data) {
    if(l == NULL) {
        return NULL;
    } else {
        List tmp = l, prev;
        Subscriber *curr = (Subscriber*)tmp->data;
        if(strncmp(curr->cl->id, data->id, strlen(data->id)) == 0) {
            l = l->next;
            free(tmp);
            if (l) {
                l->prev = NULL;
            }
            return l;
        } else {
            prev = tmp;
            tmp = tmp->next;
        }
        while(tmp != NULL) {
            curr = (Subscriber*)tmp->data;
            if(strncmp(curr->cl->id, data->id, strlen(data->id)) == 0) {
                prev->next = tmp->next;
                if (tmp->next != NULL)
                    tmp->next->prev = prev;
                free(tmp);
                return l;
            }
            prev = tmp;
            tmp = tmp->next;
        }
        return l;
    }
}

