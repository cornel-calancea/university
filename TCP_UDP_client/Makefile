
CFLAGS = -Wall -g

# Portul pe care asculta serverul (de completat)
PORT =

# Adresa IP a serverului (de completat)
IP_SERVER =

all: build
build: server subscriber

# Compileaza server.c
server: server.c list.c
	gcc ${CFLAGS} server.c list.c -o server -lm

# Compileaza subscriber.c
subscriber: subscriber.c

.PHONY: clean run_server run_subscriber

# Ruleaza serverul
run_server:
	./server ${PORT}

# Ruleaza clientul
run_subscriber:
	./subscriber ${IP_SERVER} ${PORT}

clean:
	rm -f server subscriber
