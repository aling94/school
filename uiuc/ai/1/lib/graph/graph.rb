# Represents a graph
# Stores a dictionary that maps a key to some node
# Nodes are assumed to already have their own key associated with them.

class Graph

  attr_accessor :nodes

  def initialize
    @nodes = {}
  end

  def node?(key)
    key && @nodes.key?(key)
  end

  def edge?(v, w)
    node?(v) && node?(w) && @nodes[v].adj?(@nodes[w])
  end

  def add_node(key, node)
    @nodes[key] = node unless node?(node)
  end

  def add_edge(v, w)
    @nodes[v].add(@nodes[w]) if node?(v) && node?(w)
  end

  def prn_nodes
    @nodes.each do |(x,y), _|
      puts "#{x}, #{y}"
    end
  end

  def prn_edges
    @nodes.each do |_, v|
      puts "#{v} : #{v.adj * ' '}"
    end
  end
end