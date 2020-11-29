%include "io.inc"
extern getAST
extern freeAST
extern printTree



section .bss
    ; La aceasta adresa, scheletul stocheaza radacina arborelui
    root: resd 1

section .text
global main
main:
    mov ebp, esp; for correct debugging
    ; NU MODIFICATI
    push ebp
    mov ebp, esp
    
    ; Se citeste arborele si se scrie la adresa indicata mai sus
    call getAST
    mov [root], eax;    
    ; Implementati rezolvarea aici:
    
    ;trimit parametrii si apelez functia care sa-mi printeze arborele
    ;push 0;
    ;push eax;
    ;call printTree
    ;add esp, 8;
   
    mov eax, [root]; 
    push eax;
    call evaluate;
    add esp, 4;
    ;PRINT_STRING "FINAL RESULT : ";
    PRINT_DEC 4, eax;
    ;NEWLINE;
    
    
    ; NU MODIFICATI
    ; Se elibereaza memoria alocata pentru arbore
    push dword [root]
    call freeAST
    
    xor eax, eax
    leave
    ret

evaluate:
    push ebp;
    mov ebp, esp;
    
    xor eax, eax; clear the registers that will be used
    xor ebx, ebx;
    ;xor ecx, ecx;
    
    mov eax, [ebp+8]; getting the argument - pointer to the root
    push eax; storing the root adress on the stack
    mov ebx, [eax]; ebx contains the value of data(a pointer to a string)
    
    push ebx;
    call isOperand;
    add esp, 4;
    
    cmp eax, 0;
    jz evaluate_further;
return_value:
    push ebx; will ebx always be the same?
    call toInt; the value is already stored in eax so we can return it
    add esp, 4;
     
    pop edx; pop pushed root address
    
    leave;
    ret;

evaluate_further:    
    ;if is operator
    mov edx, [ebp+8]; (top stack -) get root pointer
    mov edx, [edx+4]; make it point to left branch
        
    push edx; push left branch of tree;
    xor eax, eax;
    xor ebx, ebx;
    xor ecx, ecx;
    xor edx, edx;
    call evaluate;
    add esp, 4;
    
    mov edx, [ebp+8]; (top stack -) get root
    mov edx, [edx+8];
    push eax; push value of left branch
            
    push edx; push right branch of tree;
    xor eax, eax;
    xor ebx, ebx;
    xor ecx, ecx;
    xor edx, edx;
    call evaluate;
    add esp, 4;
    push eax;
    
    pop ebx; stores the right operand
    pop eax; stores the left operand
    mov ecx, [ebp+8];
    mov ecx, [ecx];
    
    cmp byte[ecx], 42;
    jz multiplication;
    cmp byte[ecx], 43;
    jz addition;
    cmp byte[ecx], 45;
    jz subtraction;
    cmp byte[ecx], 47;
    jz division;    
multiplication:
    imul ebx;
    jmp return_operation_result;
addition:
    add eax, ebx;
    jmp return_operation_result;
subtraction:
    sub eax, ebx;
    jmp return_operation_result;
division:
    cdq; sign-extend eax to edx;
    idiv ebx;
return_operation_result: 
    ;PRINT_STRING "EVALUATION RESULT : ";
    ;PRINT_DEC 4, eax;   
    ;NEWLINE;
    leave;
    ret;
    
    
;function that returns 1 if given string represents an operand(number)
isOperand:
    push ebp;
    mov ebp, esp;
    
    cld; 
    
    xor eax, eax;
    xor ecx, ecx;
    not ecx;
    mov edi, [ebp+8]; edi now points to the string;
    mov al, 0x00;
    repne scasb; make edi point to the character following null terminator
    sub edi, 2; edi points to the last character of the string
    cmp byte[edi], 48; see if the last character is an operator or a number
    jl no;
    mov eax, 1;
no:
    leave; 
    ret;

;function that converts a string to an integer
toInt:
    push ebp;
    mov ebp, esp;
    
    xor edx, edx; clear edx
    xor eax, eax; clear eax
    xor ecx, ecx;
    
    mov ebx, 10; multiplication factor
    mov edi, [ebp+8]; edi contains the adress of the string
    cmp byte[edi], 45; see if the first character is '-'
    jnz pushSign; push 0 on stack
    ;sets the sign bit in edx
    mov edx, 1;
    inc edi; //go to the next character
pushSign:
    push edx;
getByte:
    mul ebx;
    mov cl, byte[edi]; transfer the ascii value of the character to ecx
    sub cl, 48; convert to actual number value
    add eax, ecx;
    inc edi;
    cmp byte[edi], 0x00;
    jne getByte;
    pop edx;
    cmp edx, 0; if edx was set, we need to negate the number
    je end;
    neg eax;
end:
    xor edx, edx; clear edx
    xor ecx, ecx;
    leave;
    ret;
   
  
