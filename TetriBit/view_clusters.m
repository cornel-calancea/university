% outputs a graphical representation of the clustering solution
function view_clusters(points, centroids)
	points_number = rows(points);
	centroids_number = rows(centroids);
	
	%generam culori random pentru fiecare cluster
	colors = hsv(centroids_number);
  if centroids_number==1
    scatter3(points(:,1), points(:,2), points(:,3), [], colors)
  else
    %calculam distantele de la fiecare punct pana la cluster
    Dx = points(:,1)-centroids(:,1)';
    Dy = points(:,2)-centroids(:,2)';
    Dz = points(:,3)-centroids(:,3)';
    Distante = sqrt(Dx.^2 + Dy.^2 + Dz.^2);
    %atribuim punctului clusterul cu distanta cea mai mica
    [~, cluster] = min(Distante');
    cluster = cluster';
    %atribuim fiecarui punct culoarea corespunzatoare clusterului
    for m = 1:points_number
      points_colors(m,:) = colors(cluster(m),:);
    endfor
    scatter3(points(:,1), points(:,2), points(:,3), [], points_colors)
  endif
  %points_colors
	% TODO graphical representation coded here 
end

