#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdint.h>

typedef struct arpEntry {
    struct in_addr ip;
    uint8_t mac[6];
    struct arpEntry *next;
} arpEntry, *arpTable;

arpTable table;

arpEntry* newArpEntry();

arpEntry* getArpEntry(struct in_addr destination);

void addArpEntry(arpEntry *entry);

