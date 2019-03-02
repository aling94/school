require_relative 'graph.rb'
require_relative 'node.rb'
require 'priority_queue'

# Represents a 2D text maze
# Stores a graph, the starting node, and the goal nodes
class Maze

  attr_accessor :graph
  attr_reader :start
  attr_reader :goals

  GOAL_MARKERS = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
  SEARCH_TYPES = {D:'DFS', B:'BFS', G:'GBFS', A:'A*'}

  def initialize(file_path)
    @goals = []
    init_graph(file_path)
  end



  # Solves the maze using one of 4 strategies:
  # D: DFS, B: BFS, G: GBFS, A: A*
  def solve_XFS(type, start, goal, goals)
    # heuristic function - Manhattan Distance
    h = lambda {|(vx,vy),(wx,wy)| (vx-wx).abs + (vy-wy).abs}
    # init maps for parents and visited nodes
    cost = {}
    parent = {}
    visited = {}
    goals_reached = []

    cost[start] = 0
    parent[start] = nil
    if type == :G || type == :A
      frontier = PriorityQueue.new
      frontier[start] = 0
    else
      frontier = [start]
    end

    # Search until frontier empty, frontier data structure and expansion method depends on search type
    until frontier.empty?
      # Pick a node
      if type == :G || type == :A
        curr = frontier.delete_min[0]
      else
        curr = (type == :D)? frontier.pop : frontier.shift
      end
      visited[curr] = true

      # If goal found, return
      if curr == goal
        last = goal
        path = [goal]
        path.unshift(last) while (last = parent[last])
        expanded = visited.values.count(true)
        goals_reached += (path & goals)
        goals_reached.each {|g| goals.delete(g)}
        goals_reached << curr
        return {path: path, expanded: expanded, goal:curr, goals: goals_reached, type:type}
      end


      # Expand the node - ignore expanded nodes, add newly found nodes, and update path and parent if shorter path was found to existing frontier node
      curr.adj.each do |v|
        unless visited[v]
          dist = cost[curr] + 1
          if visited.include?(v)
            if dist < cost[v]
              cost[v] = dist
              parent[v] = curr
            end
          else
            cost[v] = dist
            parent[v] = curr
          end
          # Add to frontier
          if type == :G
            frontier[v] = h.call(v.key, goal.key)
          elsif type == :A
            frontier[v] = h.call(v.key, goal.key) + cost[v]
          else
            frontier.push(v) unless visited.include?(v)
          end
          visited[v] = false
        end
      end
    end
  end


  # Runs A* to find multiple goals
  def solve_Amulti
    # heuristic helper function - Euclidean Distance
    h = lambda {|(vx,vy),(wx,wy)| Math.sqrt((vx-wx).abs**2 + (vy-wy).abs**2)}
    goals = @goals.dup

    outputs = []
    start = @start
    until goals.empty?
      # min_cluster = goals.min_by do |g|
      #   dists = (goals-[g]).map{|gx| h.call(g.key, gx.key)}
      #   (dists.inject(:+).to_f / dists.length) * h.call(g.key, start.key)
      # end
      #goal = goals.delete(min_cluster)
      goal = goals.delete(goals.min_by{|v| h.call(v.key, start.key)})
      outputs << solve_XFS(:A, start, goal, goals)
      start = goal
    end

    all_paths = []
    goals = []
    expanded = 0
    outputs.each do |soln|
      all_paths += soln[:path]
      goals += soln[:goals]
      expanded += soln[:expanded]
    end
    construct_soln({path:all_paths, expanded:expanded}, goals)
  end



  # Helper function for printing the solution
  # Soln_map expects an array containing the path nodes and an int containing the number of expanded nodes
  def construct_soln(soln_map, goal_order)
    path = soln_map[:path]
    goals = goal_order.map{|v| v.key}
    soln_path_map = path.uniq.map{|v| v.key}.group_by{|(_,y)| y}

    puts "Number of steps: #{path.length}."
    puts "Nodes expanded: #{soln_map[:expanded]}."

    file = File.new(@maze_file, "r")
    line_num = 0
    while (line = file.gets)
      if (nodes = soln_path_map[line_num])
        nodes.each do |k|
          (x,_) = k
          line[x] = '.'
          line[x] = GOAL_MARKERS[goals.index(k)] if goals.include?(k)
        end
      end
      puts line
      line_num += 1
    end
    file.close
  end



  private


  # Initializes the maze from a text file maze
  def init_graph(file_path)
    @graph = Graph.new
    @maze_file = file_path

    file = File.new(file_path, "r")
    y = 0
    while (line = file.gets)
      x = 0
      # Create a node for each P, space, or . in the maze
      line.each_char do |c|
        case c
          when ' ', 'P', '.'
            node = Node.new([x,y])
            @goals << node if c == '.'
            @graph.add_node(node.key, node)
            @start = node if c == 'P'
        end
        x += 1
      end
      y += 1
    end
    file.close

    # Check UP, DOWN, LEFT, RIGHT
    @graph.nodes.each do |k, _|
      (x,y) = k
      [[x,y+1],[x,y-1],[x-1,y],[x+1,y]].each {|adj| @graph.add_edge(k, adj)}
    end
  end

end

