// Copyright 2019 Corneliu Calancea (cornel.calancea@gmail.com)

#ifndef TEAM_EXTRACTOR_H_
#define TEAM_EXTRACTOR_H_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "FootballClub.h"

// returns the reunion of two player, sorted by the given criterium
Player* union_lists(Player* list_A, Player* list_B, char* criteria) {
  Player *onion = NULL, *current = list_A;
  while (current != NULL) {
    Player* aux = make_player_copy(current);
    add_to_list(&onion, aux, criteria);
    current = current->next;
  }
  current = list_B;
  while (current != NULL) {
    Player* aux = make_player_copy(current);
    add_to_list(&onion, aux, criteria);
    current = current->next;
  }
  return onion;
}

Player* union_teams(FootballClub* clubs, char* club_A, char* club_B) {
  FootballClub *clubA = getClubAddress(clubs, club_A),
               *clubB = getClubAddress(clubs, club_B);
  Player *onion = NULL, *player_list_A = NULL, *player_list_B = NULL;
  if (clubA != NULL) {
    player_list_A = clubA->players;
  }
  if (clubB != NULL) {
    player_list_B = clubB->players;
  }
  onion = union_lists(player_list_A, player_list_B, "position");
  return onion;
}

Player* find_position(FootballClub* club, char* position) {
  Player* aux = club->players;
  while ((aux != NULL) && (strcmp(aux->position, position) != 0)) {
    aux = aux->next;
  }
  return aux;
}

int compare_attributes(Player* salah, Player* player) {
  if (salah->score == player->score) {
    return -strcmp(salah->name, player->name);
  } else {
    if (salah->score > player->score) {
      return 1;
    }
    return -1;
  }
}

void best(Player* salah, Player* player) {
  if (player == NULL) {
    return;
  } else {
    if (compare_attributes(salah, player) > 0) {
      return;
    }
    free(salah->name);
    salah->name = malloc(strlen(player->name) + 1);
    strcpy(salah->name, player->name);
    salah->score = player->score;
  }
}

Player* get_best_player(FootballClub* clubs, char* position) {
  FootballClub* current = clubs;

  if (clubs == NULL) {
    return NULL;
  }
  Player *salah = NULL, *aux = NULL;

  // find the first player in the given position
  while (current != NULL && aux == NULL) {
    aux = find_position(current, position);
    current = current->next;
  }

  if (aux == NULL) {
    return NULL;
  }
  salah = make_player_copy(aux);

  /*compares the best player at a given moment
      to the best player in the next club*/
  while (current != NULL) {
    best(salah, find_position(current, position));
    current = current->next;
  }

  return salah;
}

Player* get_top_players(FootballClub* clubs, int N) {
  Player* top_players = NULL;
  FootballClub* current = clubs;
  int i = 0;
  // while there are more clubs to check
  while (current != NULL) {
    Player *sorted_by_score = NULL, *first_N = NULL,
           *current_player = current->players;
    // copies the club player list, but sorts by score
    while (current_player != NULL) {
      Player* aux = make_player_copy(current_player);
      add_to_list(&sorted_by_score, aux, "score");
      current_player = current_player->next;
    }

    current_player = sorted_by_score;
    // selects the best N players for that club
    for (i = 0; i < N; ++i) {
      if (current_player != NULL) {
        Player* player = make_player_copy(current_player);
        add_to_list(&first_N, player, "score");
        current_player = current_player->next;
      }
    }

    Player *to_be_added = first_N, *next;
    // adds best N players from given club to the general top player list
    while (to_be_added) {
      next = to_be_added->next;
      add_to_list(&top_players, to_be_added, "score");
      to_be_added = next;
    }
    current = current->next;
    destroy_player_list(sorted_by_score);
  }

  return top_players;
}

Player* get_players_by_score(FootballClub* clubs, int score) {
  Player* players_by_score = NULL;
  FootballClub* current = clubs;
  while (current != NULL) {
    Player* aux = current->players;
    while (aux != NULL) {
      if (aux->score >= score) {
        Player* player = make_player_copy(aux);
        add_to_list(&players_by_score, player, "score");
      }
      aux = aux->next;
    }

    aux = current->injured_players;
    while (aux != NULL) {
      if (aux->score >= score) {
        Player* player = make_player_copy(aux);
        add_to_list(&players_by_score, player, "score");
      }
      aux = aux->next;
    }
    current = current->next;
  }
  return players_by_score;
}

Player* get_players_by_position(FootballClub* clubs, char* position) {
  Player* players_by_position = NULL;
  FootballClub* current = clubs;
  while (current != NULL) {
    Player* aux = current->players;
    while (aux != NULL) {
      if (strcmp(aux->position, position) == 0) {
        Player* player = make_player_copy(aux);
        add_to_list(&players_by_position, player, "score");
      }
      aux = aux->next;
    }

    aux = current->injured_players;
    while (aux != NULL) {
      if (strcmp(aux->position, position) == 0) {
        Player* player = make_player_copy(aux);
        add_to_list(&players_by_position, player, "score");
      }
      aux = aux->next;
    }
    current = current->next;
  }

  return players_by_position;
}

Player* get_best_team(FootballClub* clubs) {
  Player *best_team = NULL, *portar = get_players_by_position(clubs, "portar"),
         *defenders = get_players_by_position(clubs, "fundas"),
         *midfielders = get_players_by_position(clubs, "mijlocas"),
         *strikers = get_players_by_position(clubs, "atacant"),
         *aux = defenders;

  int i = 0;
  if (portar) {
    Player* player = make_player_copy(portar);
    add_to_list(&best_team, player, "score");
  }
  for (i = 0; i < 4; ++i) {
    if (aux != NULL) {
      Player* player = make_player_copy(aux);
      add_to_list(&best_team, player, "score");
      aux = aux->next;
    } else {
      break;
    }
  }
  aux = midfielders;
  for (i = 0; i < 3; ++i) {
    if (aux != NULL) {
      Player* player = make_player_copy(aux);
      add_to_list(&best_team, player, "score");
      aux = aux->next;
    } else {
      break;
    }
  }
  aux = strikers;
  for (i = 0; i < 3; ++i) {
    if (aux != NULL) {
      Player* player = make_player_copy(aux);
      add_to_list(&best_team, player, "score");
      aux = aux->next;
    } else {
      break;
    }
  }
  destroy_player_list(portar);
  destroy_player_list(midfielders);
  destroy_player_list(defenders);
  destroy_player_list(strikers);
  return best_team;
}

#endif  // TEAM_EXTRACTOR_H_INCLUDED
