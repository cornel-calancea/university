#include <stdio.h>

struct __attribute__((__packed__)) Node {
    char* data;
    struct Node* left;
    struct Node* right;
};

void printTree(struct Node *root, int space) 
{ 
	int COUNT = 3;
    // Base case 
    if (root == NULL) 
        return; 
  
    // Increase distance between levels 
    space += COUNT; 
  
    // Process right child first 
    printTree(root->right, space); 
  
    // Print current node after space 
    // count 
    printf("\n"); 
    for (int i = COUNT; i < space; i++) 
        printf(" "); 
    printf("%s\n", root->data); 
  
    // Process left child 
    printTree(root->left, space); 
} 
