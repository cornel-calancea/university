% computes a clustering solution total cost
function cost = compute_cost_pc(points, centroids)
  Dx = points(:,1)-centroids(:,1)';
     Dy = points(:,2)-centroids(:,2)';
     Dz = points(:,3)-centroids(:,3)';
     Distante = sqrt(Dx.^2 + Dy.^2 + Dz.^2);
     if rows(centroids)==1
      rowMin = Distante;
     else
       [rowMin, col] = min(Distante');
       rowMin = rowMin';
     endif
     
	cost = sum(rowMin(:,1)); 
	% TODO compute clustering solution cost
end

