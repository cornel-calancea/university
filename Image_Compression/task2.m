function task2()
  image = 'C:\Users\user\Desktop\Univer\MN\Tema 2 Checker\in\images\image1.gif'
  A = double(imread(image));
  [m n] = size(A);
  k=[1:19 20:20:99 100:30:min(m,n)];
  [U S V] = svd(A);
  sing = sum(S)
  figure
  plot(sing)
  info = sum(sing)

  for i = 1:columns(k)
    A_k = task1(image, k(i));
    S_k = S(1:k(i), 1:k(i));
    sing_k = sum(S_k);
    raport(i) = (sum(sing_k)./info);
    
    difference = (A-A_k).^2;
    error(i) = sum(sum((A-A_k).*(A-A_k)))./(m*n);
    compression_rate(i) = ((m*k(i) + n*k(i) + k(i))./(m*n));
  endfor  
  figure
  plot(k, raport)
  figure
  plot(k, error)
  figure
  plot(k, compression_rate)

endfunction
