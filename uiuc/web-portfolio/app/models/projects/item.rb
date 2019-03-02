require 'nokogiri'
require 'rails'

# Supposedly an abstract class for directories and files. Can still be instantiated but file and dir are more useful.

class Item

  attr_reader :name
  attr_reader :parent
  attr_reader :info

  # Initializes the name and parent of this item. All items are assumed to have no spaces in their path.
  # @param node XML node from an svn ls output
  def initialize(node)
    path = node.xpath('./name').text.gsub(/\s+/, '').split('/')
    @name = path.last
    @parent = path[0..-2].join('/')
  end

  # Gives the full path string of the item starting from the root of the repository.
  def full_path
    (@parent.empty?)? @name : "#{@parent}/#{@name}"
  end

  # Returns whether this object has children. Normally false, but a directory override.
  def children?
    false
  end
end
