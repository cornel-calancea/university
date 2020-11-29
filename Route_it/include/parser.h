#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdint.h>

typedef struct __attribute__((packed)) route_table_entry {
    struct in_addr prefix;
    struct in_addr next_hop;
    struct in_addr mask;
    int interface;
} routeTableEntry;


typedef struct __attribute__((packed)) route_table {
    routeTableEntry* entries;
    int size;
} routeTable;

uint64_t countLines(FILE* file);
int comparator(const void* p1, const void* p2);
int parse_table(routeTable* rTable);

routeTableEntry* getNextHop(uint32_t destination, routeTable* rTable);
