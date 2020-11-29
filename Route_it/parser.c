#include "parser.h"

uint64_t countLines(FILE* file) {
    uint64_t count_lines = 0;

    //extract character from file and store in chr
    char chr = getc(file);
    while (chr != EOF) {
        //Count whenever new line is encountered
        if (chr == '\n') {
            count_lines = count_lines + 1;
        }
        //take next character from file.
        chr = getc(file);
    }
    return count_lines;
}

int comparator(const void* p1, const void* p2) {
    routeTableEntry *e1 = (routeTableEntry*)p1, *e2 = (routeTableEntry*)p2;
    if (e1->prefix.s_addr != e2->prefix.s_addr) {
        //sort ascendingly by prefix
        return (ntohl(e1->prefix.s_addr) - ntohl(e2->prefix.s_addr));
    } else {
        //sort descendingly by mask(greatest mask comes first)
        return (ntohl(e2->mask.s_addr) - ntohl(e1->mask.s_addr));
    }
}

int parse_table(routeTable* rTable) {
    FILE* f = fopen("rtable.txt", "r");
    if (f == NULL) {
        printf("ERROR OPENING FILE!\n");
        exit(1);
    }
    rTable->size = countLines(f);
    size_t length = 0;
    char* line = NULL;
    rTable->entries = malloc(rTable->size * sizeof(routeTableEntry));
    if (rTable->entries == NULL) {
        printf("RAN OUT OF MEMORY!\n");
        exit(1);
    }
    fseek(f, 0, SEEK_SET);
    for (int i = 0; i < rTable->size; ++i) {
        getline(&line, &length, f);
        char *pref = strtok(line, " ");
        char *nextHop = strtok(NULL, " ");
        char *mask = strtok(NULL, " ");
        char *interface = strtok(NULL, " ");
        struct in_addr* testIP = malloc(sizeof(struct in_addr));
        inet_aton(mask, testIP);
        inet_aton(pref, &(rTable->entries[i].prefix));
        inet_aton(nextHop, &(rTable->entries[i].next_hop));
        inet_aton(mask, &(rTable->entries[i].mask));
        rTable->entries[i].interface = atoi(interface);
    }


    fclose(f);
    return 0;
}

routeTableEntry* getNextHop(uint32_t destination, routeTable* rTable) {
    struct in_addr test;
    test.s_addr = destination;
    routeTableEntry* entries = rTable->entries;
    int left = 0, right = rTable->size, middle, resultIndex;
    //making a binary search through the table
    while (left <= right) {
        middle = left + (right - left) / 2;
        if ((destination & entries[middle].mask.s_addr) == (entries[middle].prefix.s_addr & entries[middle].mask.s_addr)) {
            resultIndex = middle;
            while ((entries[resultIndex-1].prefix.s_addr == (destination & entries[resultIndex-1].mask.s_addr)) &&
                   (resultIndex != 0)) {
                resultIndex--;
            }
            return &(entries[resultIndex]);
        }

        if (ntohl(destination & entries[middle].mask.s_addr) < ntohl(entries[middle].prefix.s_addr)) {
            right = middle - 1;
        } else {
            left = middle + 1;
        }
    }
    return NULL;

}
