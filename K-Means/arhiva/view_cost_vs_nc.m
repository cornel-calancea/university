function view_cost_vs_nc(file_points)
	% TODO compute cost for NC = [1..10] and plot 
  for NC=1:10
    aux_C = NC;
    aux_C = num2cell(aux_C);
    [~,points] = read_input_data('cls/cluster_4.param',file_points);
    centroids = clustering_pc(points, aux_C);
    cost(NC) = compute_cost_pc(points, centroids);
  endfor
  plot(1:10, cost(1:10))
end

