// Copyright 2019 Corneliu Calancea (cornel.calancea@gmail.com)

#ifndef FOOTBALL_CLUB_H_
#define FOOTBALL_CLUB_H_

#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// List node which contains information about one football player.
typedef struct Player {
  char* name;           // player's name
  char* position;       // player's game position
  int score;            // player's score
  int injured;          // indicator for injury (1 true, 0 false)
  struct Player* next;  // next list node
  struct Player* prev;  // previous list node
} Player;

// Structure which contains information about one football club.
typedef struct FootballClub {
  char* name;                 // club's name
  Player* players;            // list with players in good shape
  Player* injured_players;    // list with injured players
  struct FootballClub* next;  // next list node
} FootballClub;

Player* make_player_copy(Player* player) {
  Player* copy = malloc(sizeof(Player));
  if (copy == NULL) {
    exit(1);
  }
  copy->name = malloc(strlen(player->name) + 1);
  copy->position = malloc(strlen(player->position) + 1);
  if ((copy->name == NULL) || (copy->position == NULL)) {
    exit(1);
  }
  strcpy(copy->name, player->name);
  strcpy(copy->position, player->position);
  copy->score = player->score;
  copy->injured = player->injured;
  copy->next = NULL;
  copy->prev = NULL;
  return copy;
}

FootballClub* add_club(FootballClub* clubs, char* name);
FootballClub* initialize_clubs(int clubs_no, char** names) {
  FootballClub* clubs = NULL;
  int i = 0;
  for (i = 0; i < clubs_no; ++i) {
    clubs = add_club(clubs, names[i]);
  }
  return clubs;
}

FootballClub* add_club(FootballClub* clubs, char* name) {
  if (clubs == NULL) {
    clubs = malloc(sizeof(FootballClub));
    if (clubs == NULL) {
      return NULL;
    }
    clubs->name = malloc(strlen(name) + 1);
    strcpy(clubs->name, name);
    clubs->players = NULL;
    clubs->injured_players = NULL;
    clubs->next = NULL;
    return clubs;
  }

  // Ne deplasam catre ultimul club din lista
  FootballClub* current = clubs;
  while (current->next != NULL) {
    current = current->next;
  }

  current->next = malloc(sizeof(FootballClub));
  // Daca alocarea nu a reusit
  if (current->next == NULL) {
    return clubs;
  }

  current->next->name = malloc(strlen(name) + 1);
  strcpy(current->next->name, name);
  current->next->players = NULL;
  current->next->injured_players = NULL;
  current->next->next = NULL;
  return clubs;
}

/*function that checks whether should go further
in a list when inserting a player*/
int go_further(Player* p1, Player* p2, char* criteria) {
  if (strcmp(criteria, "position") == 0) {
    if (strcmp(p1->position, p2->position) != 0) {
      return strcmp(p1->position, p2->position);
    }
  }

  if (p1->score != p2->score) {
    return ((p1->score) < (p2->score));
  } else {
    return strcmp(p1->name, p2->name);
  }
}

// Function that returns the element after whom needs to be inserted out player
Player* get_position(Player* list, Player* player, char* criteria) {
  Player* current = list;
  // Daca lista e goala sau trebuie sa inseram la inceputul listei
  if ((current == NULL) || (go_further(player, current, criteria) <= 0)) {
    return NULL;
  }

  // lista nu e nula, deci avem element nenul dupa care vom insera
  while (current != NULL) {
    // daca e ultimul element din lista sau cel dupa care trebuie inserat
    if ((current->next == NULL) ||
        (go_further(player, current->next, criteria) <= 0)) {
      return current;
    }
    current = current->next;
  }
  return NULL;
}

/*returns pointer to the club that we search*/
FootballClub* getClubAddress(FootballClub* clubs, char* club_name) {
  FootballClub* ourclub = clubs;
  if (ourclub != NULL) {
    while (strcmp(club_name, ourclub->name) != 0) {
      if (ourclub->next != NULL) {
        ourclub = ourclub->next;
      } else {
        return NULL;
      }
    }
  }
  return ourclub;
}

Player* getInjuredPlayerAddress(FootballClub* ourclub, char* player_name) {
  Player* aux = ourclub->injured_players;
  while (aux != NULL) {
    if (strcmp(aux->name, player_name) == 0) {
      return aux;
    }
    aux = aux->next;
  }
  return NULL;
}

Player* getPlayerAddress(FootballClub* ourclub, char* player_name) {
  Player* ourplayer = ourclub->players;
  if (ourplayer != NULL) {
    while (strcmp(ourplayer->name, player_name) != 0) {
      if (ourplayer->next == NULL) {
        ourplayer = getInjuredPlayerAddress(ourclub, player_name);
        return ourplayer;
      }
      ourplayer = ourplayer->next;
    }
  }

  return ourplayer;
}

void add_to_list(Player** list, Player* aux, char* criteria) {
  // obtinem adresa jucatorului dupa care trebuie sa adaugam
  Player* previous = get_position(*list, aux, criteria);
  aux->next = NULL;
  aux->prev = NULL;

  if (previous == NULL) {  // daca trebuie sa adaugam la inceputul listei
    aux->next = *list;
    if (*list != NULL) {
      (*list)->prev = aux;
    }
    *list = aux;

    return;
  } else {
    if (previous->next == NULL) {  // daca trebuie sa inseram la sfarsit
      previous->next = aux;
      aux->prev = previous;
      aux->next = NULL;
      return;
    } else {  // Daca trebuie sa inseram la mijloc
      aux->prev = previous;
      aux->next = previous->next;
      previous->next->prev = aux;
      previous->next = aux;
      return;
    }
  }
}

void add_player(FootballClub* clubs, char* club_name, char* player_name,
                char* position, int score) {
  FootballClub* ourclub = getClubAddress(clubs, club_name);
  if (ourclub == NULL) {
    return;
  }

  Player* aux = malloc(sizeof(Player));
  if (aux == NULL) {
    exit(1);
  }

  aux->name = malloc(strlen(player_name) + 1);
  if (aux->name == NULL) {
    exit(1);
  }
  strcpy(aux->name, player_name);

  aux->position = malloc(strlen(position) + 1);
  if (aux->position == NULL) {
    exit(1);
  }
  strcpy(aux->position, position);
  aux->score = score;
  aux->injured = 0;
  aux->prev = NULL;
  aux->next = NULL;

  add_to_list(&(ourclub->players), aux, "position");
}

void add_to_injured_list(FootballClub* club, Player* calicul_nostru);
void remove_injured_player(FootballClub* club, Player* aux);
void remove_player(FootballClub* clubs, char* club_name, char* player_name);
void transfer_player(FootballClub* clubs, char* player_name, char* old_club,
                     char* new_club) {
  FootballClub *prev_club = getClubAddress(clubs, old_club),
               *next_club = getClubAddress(clubs, new_club);
  if (prev_club == NULL || next_club == NULL) {
    exit(1);
  }

  Player* player_address = getPlayerAddress(prev_club, player_name);

  if (player_address != NULL) {
    Player* aux = make_player_copy(player_address);
    if (aux->injured == 0) {
      add_to_list(&(next_club->players), aux, "position");
      remove_player(clubs, prev_club->name, player_name);
      return;
    } else {
      remove_injured_player(prev_club, player_address);
      add_to_injured_list(next_club, aux);
      return;
    }
  } else {
    player_address = getInjuredPlayerAddress(prev_club, player_name);
    if (player_address) {
      Player* aux = make_player_copy(player_address);
      if (aux != NULL) {
        add_to_injured_list(next_club, aux);
        remove_injured_player(prev_club, player_address);
        return;
      }
    }
  }
}

void remove_player(FootballClub* clubs, char* club_name, char* player_name) {
  FootballClub* ourclub = getClubAddress(clubs, club_name);
  if (ourclub == NULL) {
    return;
  }

  Player* to_be_removed = getPlayerAddress(ourclub, player_name);
  if (to_be_removed == NULL) {
    to_be_removed = getInjuredPlayerAddress(ourclub, player_name);
    if (to_be_removed == NULL) {
      return;
    }
    remove_injured_player(ourclub, to_be_removed);
    return;
  }

  if (to_be_removed->prev != NULL) {
    to_be_removed->prev->next = to_be_removed->next;
  } else {
    ourclub->players = to_be_removed->next;
  }

  if (to_be_removed->next != NULL) {
    to_be_removed->next->prev = to_be_removed->prev;
  }
  free(to_be_removed->name);
  free(to_be_removed->position);
  free(to_be_removed);
}

void update_score(FootballClub* clubs, char* club_name, char* player_name,
                  int score) {
  FootballClub* myclub = getClubAddress(clubs, club_name);
  if (myclub == NULL) {
    return;
  }
  Player* player = getPlayerAddress(myclub, player_name);
  if (player == NULL) {
    return;
  }

  Player* aux = make_player_copy(player);
  aux->score = score;
  if (aux->injured == 0) {
    remove_player(clubs, club_name, player_name);
    add_to_list(&(myclub->players), aux, "position");
  } else {
    remove_injured_player(myclub, player);
    add_to_injured_list(myclub, aux);
  }
}

void update_game_position(FootballClub* clubs, char* club_name,
                          char* player_name, char* position, int score) {
  FootballClub* ourclub = getClubAddress(clubs, club_name);
  Player *ourplayer = getPlayerAddress(ourclub, player_name),
         *aux = make_player_copy(ourplayer);

  // replacing the updated attributes
  free(aux->position);
  aux->position = malloc(strlen(position) + 1);
  strcpy(aux->position, position);
  aux->score = score;

  if (aux->injured == 0) {
    remove_player(clubs, club_name, player_name);
    add_to_list(&(ourclub->players), aux, "position");
  } else {
    remove_injured_player(ourclub, ourplayer);
    add_to_injured_list(ourclub, aux);
  }
}

// keeps a player's score within [-100, 100]
void stay_within_limits(int* n) {
  if ((*n >= -100) && (*n <= 100)) {
    return;
  }
  if (*n < -100) {
    *n = -100;
    return;
  }
  if (*n > 100) {
    *n = 100;
  }
}

void add_to_injured_list(FootballClub* club, Player* calicul_nostru) {
  if (club->injured_players == NULL) {
    club->injured_players = calicul_nostru;
    return;
  }

  Player* aux = club->injured_players;
  while (aux->next != NULL) {
    aux = aux->next;
  }
  aux->next = calicul_nostru;
}

void add_injury(FootballClub* clubs, char* club_name, char* player_name,
                int days_no) {
  FootballClub* myclub = getClubAddress(clubs, club_name);
  if (myclub == NULL) {
    return;
  }
  Player* player = getPlayerAddress(myclub, player_name);
  if (player == NULL) {
    return;
  }

  Player* aux = make_player_copy(player);
  aux->injured = 1;
  remove_player(clubs, club_name, player_name);
  // making a cast to int will truncate the number
  aux->score = (int)(aux->score - (days_no * 0.1));
  stay_within_limits(&aux->score);
  add_to_injured_list(myclub, aux);
}

void remove_injured_player(FootballClub* club, Player* aux) {
  if (club->injured_players == aux) {
    club->injured_players = aux->next;
    free(aux->name);
    free(aux->position);
    free(aux);
    return;
  }

  Player* previous = club->injured_players;
  while (previous->next != aux) {
    previous = previous->next;
  }
  previous->next = aux->next;
  free(aux->name);
  free(aux->position);
  free(aux);
}

void recover_from_injury(FootballClub* clubs, char* club_name,
                         char* player_name) {
  FootballClub* myclub = getClubAddress(clubs, club_name);
  if (myclub == NULL) {
    return;
  }
  Player* aux = getInjuredPlayerAddress(myclub, player_name);
  if (aux == NULL) {
    return;
  }
  add_player(clubs, club_name, player_name, aux->position, aux->score);
  remove_injured_player(myclub, aux);
}

// Frees memory for a list of Player.

void destroy_player_list(Player* player) {
  Player *prev = player, *aux = player;
  while (prev != NULL) {
    aux = prev->next;
    free(prev->name);
    prev->name = NULL;
    free(prev->position);
    prev->position = NULL;
    prev->prev = NULL;
    prev->next = NULL;
    free(prev);
    prev = aux;
  }
  free(aux);
  aux = NULL;
  player = NULL;
}

// Frees memory for a list of FootballClub.
void destroy_club_list(FootballClub* clubs) {
  FootballClub* aux = clubs;
  while (clubs != NULL) {
    clubs = clubs->next;
    destroy_player_list(aux->players);
    destroy_player_list(aux->injured_players);
    free(aux->name);
    aux->name = NULL;
    free(aux);
    aux = clubs;
  }
  free(clubs);
  clubs = NULL;
}

// Displays a list of players.
void show_list(FILE* f, Player* players, int free_memory) {
  fprintf(f, "P: ");
  Player* player = players;
  while (player) {
    fprintf(f, "(%s, %s, %d, %c) ", player->name, player->position,
            player->score, player->injured ? 'Y' : '_');
    player = player->next;
  }
  if (free_memory) {
    destroy_player_list(players);
  }
  fprintf(f, "\n");
}

// Displays a list of players in reverse.
void show_list_reverse(FILE* f, Player* players, int free_memory) {
  fprintf(f, "P: ");
  Player* player = players;
  if (player) {
    while (player->next) {
      player = player->next;
    }
    while (player) {
      fprintf(f, "(%s, %s, %d, %c) ", player->name, player->position,
              player->score, player->injured ? 'Y' : '_');
      player = player->prev;
    }
  }
  if (free_memory) {
    destroy_player_list(players);
  }
  fprintf(f, "\n");
}

// Displays information about a football club.
void show_clubs_info(FILE* f, FootballClub* clubs) {
  fprintf(f, "FCs:\n");
  while (clubs) {
    fprintf(f, "%s\n", clubs->name);
    fprintf(f, "\t");
    show_list(f, clubs->players, 0);
    fprintf(f, "\t");
    show_list(f, clubs->injured_players, 0);
    clubs = clubs->next;
  }
}

#endif  // FOOTBALL_CLUB_H_INCLUDED
