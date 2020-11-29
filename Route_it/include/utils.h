//
// Created by cornel on 3/21/20.
//
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/ioctl.h>
#include <netdb.h>
#include <netinet/ip_icmp.h>
#include <netinet/in.h>
#include <linux/if_ether.h>
#include <arpa/inet.h>
#include <net/ethernet.h>
#include <net/if.h>
#include <ifaddrs.h>

uint16_t rfc1624(uint16_t oldCheck, uint16_t oldValue, uint16_t newValue);
uint16_t checksum(void *vdata, size_t length);