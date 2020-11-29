#include "arpTable.h"

arpEntry* newArpEntry() {
    arpEntry *entry = malloc(sizeof(arpEntry));
    entry->next = NULL;
    return entry;
}

arpEntry* getArpEntry(struct in_addr destination) {
    arpEntry* entry = table;
    while (entry) {
        if (entry->ip.s_addr == destination.s_addr) {
            return entry;
        }
        entry = entry->next;
    }
    return NULL;
}

void addArpEntry(arpEntry *entry) {
    if (entry->next == NULL) {
        printf("BAD ARGUMENT!\n");
    }
    if (table == NULL) {
        table = entry;
        return;
    }
    entry->next = table;
    table = entry;
}