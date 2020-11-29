CC = gcc
CFLAGS = -g -Wall -Wextra

all: main

.PHONY: main
main: main.c parson.c helpers.c buffer.c
	$(CC) $(CFLAGS) -o $@ main.c parson.c helpers.c buffer.c

clean:
	rm -f main *.o
