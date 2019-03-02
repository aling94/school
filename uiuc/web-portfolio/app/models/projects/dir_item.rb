require_relative 'item'

# Represents a dir described by an svn list output.
# Revision information assumes node comes from svn log output.

class DirItem < Item

  attr_reader :children

  # Creates a DirItem from an svn list <entry> node
  # @param node XML node
  def initialize(node)
    super(node)
    @children = []
    rev = node.xpath("./commit").first['revision'].to_i
    date = node.xpath("./commit/date").text.split('T')[0]
    @info = {type: 'dir', rev: rev, date: date}
  end

  # Adds a child to the directory
  # @param child Item representing a child, should be Dir or File
  def add_child(child)
    @children << child unless @children.include?(child)
  end

  def children?
    !children.empty?
  end
end
