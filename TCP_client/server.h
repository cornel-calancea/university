//
// Created by cornel on 4/25/20.
//
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>
#include <dirent.h>
#include <sys/stat.h>
#include "helpers.h"
#include "list.h"

#ifndef PCOM_HW2_UDP_CLIENT_SERVER_H
#define PCOM_HW2_UDP_CLIENT_SERVER_H

#endif //PCOM_HW2_UDP_CLIENT_SERVER_H
#define MAX_ID_LENGTH 10
#define MAX_TOPIC_LENGTH 50
#define MAX_DATA_LENGTH 1500
#define MAX_MESSAGE_LEN 1560
#define INT 0
#define SHORT_REAL 1
#define FLOAT 2
#define STRING 3
#define TYPE_START 50
#define DATA_START 51
char buffer[BUFLEN];

typedef struct client {
    char id[MAX_ID_LENGTH];
    int active;
    int socket;
}Client;

typedef struct subscriber {
    Client *cl;
    int SF;
}Subscriber;

typedef struct topic {
    char name[MAX_TOPIC_LENGTH];
    List subscribers;
}Topic;

void printSubscriberList(List subscribers);
void printTopicList();
int rmrf(char *path);
Topic *getTopic(char *topicName);
void subscribe(Client *cl, char *topic, int sf);
void unsubscribe(Client *cl, char *topic);
Client *getClientById(char *id);
Client *getClientBySock(int sockfd);
//function that sends the message with given length to client c
void sendMessage(Client *c, char *message);
void sendPendingMessages(Client *subscriber);
void addClientTCP(char *id, int length, int sockfd);
void addPendingMessage(Client *cl, char *message);
void distributeTopicUpdate(char *topic, char *message);
void handleUdpMessage(char *message, struct sockaddr_in cli_addr);
void handleMessageTCP(Client *cl, char *message);
void freeClientList();
void freeSubscriberList(List l);
void freeTopicList();
void endSession(int fdMax);
void usage(char *file);
int max(int n1, int n2);
List deleteItem(List list, Client *data);