function [A_k S]=task4(image, k)
  A = double(imread(image));
  [m n] = size(A);
  for i = 1:m
  miu(i) = mean(A(i, :));
  endfor
  for i = 1:m
    A(i,:) = A(i,:)-miu(i);
  endfor
  Z = (1/(n-1)).*A*A';
  [V S] = eig(Z);
  W = V(:, 1:k);
  Y = (W')*A;
   A_k = W*Y;
   for i = 1:m
      A_k(i, :) = A_k(i, :) + miu(i);
   endfor
endfunction
