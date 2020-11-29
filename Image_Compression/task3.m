function [A_k S] = task3(image, k)
  A = double(imread(image));
  [m n] = size(A);
  for i = 1:m
  miu(i) = mean(A(i, :));
  endfor
  
  for i = 1:m
    A(i,:) = A(i,:)-miu(i);
  endfor
  Z = (1/sqrt(n-1)).*(A');
  [U S V] = svd(Z);
  W = V(:, 1:k);
  Y = (W')*A;
  A_k = W*Y;
  for i = 1:m
    A_k(i, :) = A_k(i, :) + miu(i);
  endfor
endfunction
