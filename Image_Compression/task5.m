function task5()
  image = 'C:\Users\user\Desktop\Univer\MN\Tema 2 Checker\in\images\image3.gif'
  A = double(imread(image));
  [m n] = size(A);
  k=[1:19 20:20:99 100:30:min(m,n)];
  [A_k S] = task3(image, k);
  figure;
  plot(diag(S))
  for i = 1:columns(k)
    [A_k S] = task3(image, k(i));
    S_sum = sum(diag(S));
    S_k = S(1:k(i), 1:k(i));
    S_k_sum = sum(diag(S_k)); 
    info(i) = S_k_sum./S_sum;
    error(i) = sum(sum((A-A_k).*(A-A_k)))./(m*n);
    compression_rate(i) = (2*k(i)+1)./n;
  endfor
  figure
  plot(k, info)
  figure
  plot(k, error)
  figure
  plot(k, compression_rate)
endfunction
