% reads cluster count and points from input files 
function [NC points] = read_input_data(file_params, file_points)
	
	% set these values correctly
  fid = fopen(file_params, 'r');
	NC = textscan(fid, '%d');
  fclose(fid);
  fid = fopen(file_points, 'r');
	%points = textscan(fid, '%f %f %f', 'HeaderLines', 6);
  points = dlmread(fid, ' ', 5, 0);
  fclose(fid);

	% TODO read NC
	% TODO read points
end

