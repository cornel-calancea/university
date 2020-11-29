% computes the NC centroids corresponding to the given points using K-Means
function centroids = clustering_pc(points, NC)
  [points_number, columns] = size(points);
  NC = cell2mat(NC);
  centroids_new =zeros(NC, 3);
  %ne intereseaza sa nu avem doua puncte cu aceleasi coordonate
  %alegem centroizii ca fiind primele NC puncte, care sunt unice
  points = unique(points, 'rows');
  centroids = points(1:NC, :);
  diferenta = centroids_new-centroids; 
if NC == 1
   centroids = mean(points, 1);
else
   %repetam pana obtinem o eroare satisfacatoare
  do
     Dx = points(:,1)-centroids(:,1)';
     Dy = points(:,2)-centroids(:,2)';
     Dz = points(:,3)-centroids(:,3)';
     Distante = sqrt(Dx.^2 + Dy.^2 + Dz.^2);
     
     [~, cluster] = min(Distante');
     cluster = cluster';
     %pentru fiecare centroid creez o matrice cu punctele ce ii corespund
    for i = 1:NC 
      clear aux_points;
      aux_points = [];
      for m = 1:points_number
        if (cluster(m)==i)
        aux_points = [aux_points; points(m, :)];
        endif
      endfor
      %pozitia noua a centroidului este media fiecarei componente a punctelor
      centroids_new(i,:) = mean(aux_points,1);
    endfor
    %verificam eroarea
    diferenta = centroids_new-centroids;
    centroids = centroids_new;
  until max(diferenta) < 10^-6
endif
 % TODO K-Means code 
end
