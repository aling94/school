# Represents a maze node
# Initialized with a key used to refer to itself.
# Stores an adjacency list.

class Node

  attr_reader :key
  attr_accessor :adj

  def initialize(key)
    @key = key
    @adj  = []
  end

  def add(node)
    @adj << node if node && !@adj.include?(node)
  end

  def adj?(node)
    node && @adj.include?(node)
  end

  def to_s
    (x,y) = @key
    "(#{x},#{y})"
  end

end
