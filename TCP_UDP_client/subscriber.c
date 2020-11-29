#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include "helpers.h"

void usage(char *file) {
    fprintf(stderr, "Usage: %s server_address server_port\n", file);
    exit(0);
}

void showReceivedMessages(char *buffer, int totalReceived) {
    int offset = 0;
    while (offset < totalReceived) {
        int *messageLen = (int *) (buffer + offset);
        offset += sizeof(int);
        for (int i = 0; i < *messageLen; ++i) {
            printf("%c", buffer[offset + i]);
        }
        printf("\n");
        offset += *messageLen;
    }
}

int main(int argc, char *argv[]) {
    int sockfd, n, ret;
    struct sockaddr_in serv_addr;
    char buffer[BUFLEN];
    //variabile pentru file-descriptorul maxim,
    // multimea de fd-uri si o multime auxiliara de fd-uri
    int maxFD;
    fd_set read_fd, temp_fd;
    //initializez multimile
    FD_ZERO(&read_fd);
    FD_ZERO(&temp_fd);

    if (argc < 4) {
        usage(argv[0]);
    }
    char *myID = argv[1];
    char *ipServer = argv[2];
    char *port = argv[3];

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    DIE(sockfd < 0, "socket");
    //adaugam socketul si tastatura la multimea de fd-uri
    FD_SET(sockfd, &read_fd);
    FD_SET(STDIN_FILENO, &read_fd);
    maxFD = sockfd;

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(atoi(port));
    ret = inet_aton(ipServer, &serv_addr.sin_addr);
    DIE(ret == 0, "inet_aton");

    ret = connect(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr));
    DIE(ret < 0, "connect");
    //trimit id-ul meu catre server
    memset(buffer, 0, BUFLEN);
    strcpy(buffer, myID);
    n = send(sockfd, buffer, strlen(buffer), 0);
    DIE(n < 0, "send");


    while (1) {
        //resetez multimea auxiliara, pt ca a fost modificata de select
        temp_fd = read_fd;
        ret = select(maxFD + 1, &temp_fd, NULL, NULL, NULL);
        DIE(ret < 0, "No fds selected");

        //verific fiecare fisier
        for (int i = 0; i <= maxFD; ++i) {
            if (FD_ISSET(i, &temp_fd)) {
                if (STDIN_FILENO == i) {
                    //daca citim de la tastatura
                    //curat bufferul si citesc de la stdin
                    memset(buffer, 0, BUFLEN);
                    fgets(buffer, BUFLEN, stdin);

                    // daca trebuie inchisa conexiunea
                    if (strncmp(buffer, "exit", 4) == 0) {
                        FD_CLR(i, &read_fd);
                        FD_CLR(i, &temp_fd);
                        FD_CLR(sockfd, &read_fd);
                        FD_CLR(sockfd, &temp_fd);
                        close(sockfd);
                        return 0;
                    }
                    n = send(sockfd, buffer, strlen(buffer), 0);
                    DIE(n < 0, "send");
                    continue;
                }
                if (i == sockfd) {
                    //daca am primit mesaj de la server
                    memset(buffer, 0, BUFLEN);
                    n = recv(sockfd, buffer, sizeof(buffer), 0);
                    if (n == 0) {
                        printf("Connection closed by server!\n");
                        return 0;
                    }
                    showReceivedMessages(buffer, n);
                }
            }
        }

    }

}
