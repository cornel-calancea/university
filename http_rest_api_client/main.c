#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>
#include "parson.h"
#include "helpers.h"

#define PORT 8080
#define CMD_REGISTER 1
#define CMD_LOGIN 2
#define CMD_ENTERLIB 3
#define CMD_GETALL 4
#define CMD_GETBOOK 5
#define CMD_ADDBOOK 6
#define CMD_DELETE 7
#define CMD_LOGOUT 8
#define CMD_EXIT 9
#define TARGET_REGISTER "/api/v1/tema/auth/register"
#define TARGET_LOGIN "/api/v1/tema/auth/login"
#define TARGET_ENTERLIB "/api/v1/tema/library/access"
#define TARGET_BOOKS "/api/v1/tema/library/books"
#define TARGET_LOGOUT "/api/v1/tema/auth/logout"

char *hostAddr = "ec2-3-8-116-10.eu-west-2.compute.amazonaws.com";
char *contentType = "application/json; charset=utf-8";
char *httpVersion = "HTTP/1.1";
char cookie[500];
char jwt[500];
char message[BUFLEN];
int sockfd;
struct in_addr servIP;

//function to show a JSON array/object
void showJSON(char *json) {
    if (strcmp(json, "[]") == 0) {
        printf("-------------------------------------\n");
        printf("There are no books to display!\n");
        return;
    }
    for (int i = 0; json[i] != 0; ++i) {
        if ((json[i] == '[') || (json[i] == ']') || (json[i] == '"') || (json[i] == '}')) {
            continue;
        }
        if (json[i] == '{') {
            printf("-------------------------------------\n");
            continue;
        }
        if (json[i] == ',') {
            printf("\n");
            continue;
        }
        printf("%c", json[i]);
    }
    printf("\n");
}

//receives a message, shows the HTTP response code and the content of the JSON(if there is any)
void receiveMessage() {
    char *httpResponse = receive_from_server(sockfd);
    char responseCode[20];
    //extracting and showing the HTTP response code
    memcpy(responseCode, strstr(httpResponse, " "), 20);
    strtok(responseCode, "\n");
    printf("\n-------------------------------------\nResponse code :%s\n", responseCode);
    //check if there is a cookie, and set it in the global variable
    char *cookieLocation = strstr(httpResponse, "Set-Cookie:");
    if (cookieLocation) {
        cookieLocation = strstr(cookieLocation, " ") + 1;
        sscanf(cookieLocation, "%[^\n]", cookie);
        memset(cookie + strlen(cookie) - 1, 0, 1);
    }
    //check if there is a JSON array
    char *array = strstr(httpResponse, "[");
    if (array) {
        showJSON(array);
    } else {
        //check if there is a JSON object
        char *json = strstr(httpResponse, "{");
        if (json) {
            //check if there is a JWT and set it in the global variable
            JSON_Value *rootVal = json_parse_string(json);
            JSON_Object *obj = json_object(rootVal);
            JSON_Value *tokValue = json_object_get_value(obj, "token");
            if (tokValue) {
                const char* token = json_value_get_string(tokValue);
                strcpy(jwt, token);
            }
            showJSON(json);
        }
    }
    printf("-------------------------------------\n\n");
}

//composes a GET request for the given target, sends it, and receives the answer
void sendGetRequest(char *target) {
    char line[600];
    memset(message, 0, BUFLEN);
    sprintf(line, "GET %s %s", target, httpVersion);
    compute_message(message, line);
    sprintf(line, "Host: %s", hostAddr);
    compute_message(message, line);
    compute_message(message, "Connection: Keep-Alive");
    //if there is a cookie, add it
    if (strlen(cookie) > 0) {
        sprintf(line, "Cookie: %s", cookie);
        compute_message(message, line);
    }
    //if there is a jwt, add it
    if (strlen(jwt) > 0) {
        sprintf(line, "Authorization: Bearer %s", jwt);
        compute_message(message, line);
    }
    compute_message(message, "");
    //opening a connection, sending the message, getting the response, and closing the connection
    sockfd = open_connection(inet_ntoa(servIP), PORT, AF_INET, SOCK_STREAM, 0);
    send_to_server(sockfd, message);
    receiveMessage();
    close(sockfd);
}

//composes a POST request for the given target, sends it, and receives the answer
void sendPostRequest(char *target, JSON_Value *rootValue) {
    char line[600];
    memset(message, 0, BUFLEN);
    sprintf(line, "POST %s %s", target, httpVersion);
    compute_message(message, line);
    sprintf(line, "Host: %s", hostAddr);
    compute_message(message, line);
    compute_message(message, "Connection: Keep-Alive");
    //adding the cookie
    if (strlen(cookie) > 0) {
        sprintf(line, "Cookie: %s", cookie);
        compute_message(message, line);
    }
    //adding the JWT to the request
    if (strlen(jwt) > 0) {
        sprintf(line, "Authorization: Bearer %s", jwt);
        compute_message(message, line);
    }
    //check if the given JSON is valid and adding it to the request
    if (!rootValue) {
        printf("Error at POST request - no JSON given!\n");
        return;
    }
    char *serialized = json_serialize_to_string(rootValue);
    sprintf(line, "Content-Length: %lu", strlen(serialized));
    compute_message(message, line);
    sprintf(line, "Content-Type: %s", contentType);
    compute_message(message, line);
    compute_message(message, "");
    compute_message(message, serialized);
    compute_message(message, "");
    sockfd = open_connection(inet_ntoa(servIP), PORT, AF_INET, SOCK_STREAM, 0);
    send_to_server(sockfd, message);
    receiveMessage();
    close(sockfd);
}

//composes a DELETE request for the given target, sends it, and receives the answer
void sendDeleteRequest(char *target) {
    char line[600];
    memset(message, 0, BUFLEN);
    sprintf(line, "DELETE %s %s", target, httpVersion);
    compute_message(message, line);
    sprintf(line, "Host: %s", hostAddr);
    compute_message(message, line);
    compute_message(message, "Connection: Keep-Alive");
    if (strlen(cookie) > 0) {
        sprintf(line, "Cookie: %s", cookie);
        compute_message(message, line);
    }
    if (strlen(jwt) > 0) {
        sprintf(line, "Authorization: Bearer %s", jwt);
        compute_message(message, line);
    }
    compute_message(message, "");
    sockfd = open_connection(inet_ntoa(servIP), PORT, AF_INET, SOCK_STREAM, 0);
    send_to_server(sockfd, message);
    receiveMessage();
    close(sockfd);
}

void createAccount() {
    char user[100], password[100];
    printf("username=");
    scanf("%s", user);
    printf("password=");
    scanf("%s", password);
    //creating the JSON object to be sent
    JSON_Object *jsonObject = NULL;
    JSON_Value *rootValue = json_value_init_object();
    jsonObject = json_value_get_object(rootValue);
    json_object_set_string(jsonObject, "username", user);
    json_object_set_string(jsonObject, "password", password);
    sendPostRequest(TARGET_REGISTER, rootValue);
}

void login() {
    char user[100], password[100];
    printf("username=");
    scanf("%s", user);
    printf("password=");
    scanf("%s", password);
    JSON_Object *jsonObject = NULL;
    JSON_Value *rootValue = json_value_init_object();
    jsonObject = json_value_get_object(rootValue);
    json_object_set_string(jsonObject, "username", user);
    json_object_set_string(jsonObject, "password", password);
    sendPostRequest(TARGET_LOGIN, rootValue);
}

void enterLibrary() {
    sendGetRequest(TARGET_ENTERLIB);
}

void getAllBooks() {
    sendGetRequest(TARGET_BOOKS);
}

void getBook() {
    int id;
    printf("id=");
    scanf("%d", &id);
    char target[300];
    sprintf(target, "%s/%d", TARGET_BOOKS, id);
    sendGetRequest(target);
}

void addBook() {
    char title[100], author[200], genre[100], publisher[100];
    int pageCount;
    printf("title=");
    scanf("%s", title);
    printf("author=");
    scanf("%s", author);
    printf("genre=");
    scanf("%s", genre);
    printf("publisher=");
    scanf("%s", publisher);
    printf("page_count=");
    scanf("%d", &pageCount);
    JSON_Object *jsonObject = NULL;
    JSON_Value *rootValue = json_value_init_object();
    jsonObject = json_value_get_object(rootValue);
    json_object_set_string(jsonObject, "title", title);
    json_object_set_string(jsonObject, "author", author);
    json_object_set_string(jsonObject, "genre", genre);
    json_object_set_number(jsonObject, "page_count", pageCount);
    json_object_set_string(jsonObject, "publisher", publisher);

    sendPostRequest(TARGET_BOOKS, rootValue);
}

void deleteBook() {
    int id;
    printf("id=");
    scanf("%d", &id);
    char target[300];
    sprintf(target, "%s/%d", TARGET_BOOKS, id);
    sendDeleteRequest(target);
}

void logout() {
    sendGetRequest(TARGET_LOGOUT);
}

void quit() {
    printf("-----------------------------------------------------------\n");
    printf("----------<< BooKornel wishes you a great day! >>----------\n");
    printf("-----------------------------------------------------------\n");
    close(sockfd);
}

// Receives a name and prints IP addresses
struct in_addr get_ip(char *name) {
    struct addrinfo hints, *result;

    //setting hints
    memset(&hints, 0, sizeof(struct addrinfo));
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_protocol = IPPROTO_TCP;

    //getting addreses
    getaddrinfo(name, NULL, &hints, &result);
    //iterating through them and finding a good one
    struct addrinfo *aux = result;
    while (aux) {
        if (result->ai_family == AF_INET) {
            struct sockaddr_in *ip = (struct sockaddr_in *) (result->ai_addr);
            return ip->sin_addr;
        }
        aux = aux->ai_next;
    }
    printf("IPv4 not found!!!\n");
    exit(1);
}

/*parses the string and returns the code of the command
 * if the command is invalid, returns (-1) */
int getCommand() {
    char command[100];
    scanf("%s", command);
    if (strcmp(command, "register") == 0) {
        return CMD_REGISTER;
    }
    if (strcmp(command, "login") == 0) {
        return CMD_LOGIN;
    }
    if (strcmp(command, "enter_library") == 0) {
        return CMD_ENTERLIB;
    }
    if (strcmp(command, "get_books") == 0) {
        return CMD_GETALL;
    }
    if (strcmp(command, "get_book") == 0) {
        return CMD_GETBOOK;
    }
    if (strcmp(command, "add_book") == 0) {
        return CMD_ADDBOOK;
    }
    if (strcmp(command, "delete_book") == 0) {
        return CMD_DELETE;
    }
    if (strcmp(command, "logout") == 0) {
        return CMD_LOGOUT;
    }
    if (strcmp(command, "exit") == 0) {
        return CMD_EXIT;
    }
    return -1;

}

int main() {
    printf("-----------------------------------------------------------\n");
    printf("--------<< Welcome to the BooKornel MegaClient! >>---------\n");
    printf("-----------------------------------------------------------\n");
    //getting the host IP
    servIP = get_ip(hostAddr);
    while (1) {
        printf("Enter a command : \n");
        int command = getCommand();
        switch (command) {
            case CMD_REGISTER:
                createAccount();
                break;
            case CMD_LOGIN:
                login();
                break;
            case CMD_ENTERLIB:
                enterLibrary();
                break;
            case CMD_GETALL:
                getAllBooks();
                break;
            case CMD_GETBOOK:
                getBook();
                break;
            case CMD_ADDBOOK:
                addBook();
                break;
            case CMD_DELETE:
                deleteBook();
                break;
            case CMD_LOGOUT:
                logout();
                break;
            case CMD_EXIT:
                quit();
                return 0;
            default:
                printf("Unknown command!\n");
        }
    }
}

