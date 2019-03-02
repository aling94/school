require_relative 'item'

# Represents a file described by an svn list output.
# Revision information assumes node comes from svn log output.

class FileItem < Item

  attr_reader :revs

  SVN_RT = 'https://subversion.ews.illinois.edu/svn/sp16-cs242/aling3/'

  # Creates a FileItem from an svn list <entry> node
  # @param node XML node
  def initialize(node)
    super(node)
    @info = {size: node.xpath('./size').text.to_i}
    set_mime_type
    @revs = {}
  end

  # Adds a revision to the file's revision list from an svn log logentry node.
  # @param entry svn log logentry node
  def add_rev(entry)
    rev = entry['revision'].to_i
    athr = entry.xpath('./author').first.text
    msg = entry.xpath('./msg').first.text
    date = entry.xpath("./date").first.text.split('T')[0]
    @revs[rev] = {date: date, author: athr, msg: msg}
  end

  # Returns the full svn path
  # @return String
  def svn_path
    "#{SVN_RT}#{full_path}"
  end

  private
  # Helper to set the file type based on extension
  def set_mime_type
    ext = @name.split('.').last
    ext = '' if ext == @name
    type = Rack::Mime.mime_type('.'+ ext).to_s
    @info[:type] = (type == 'application/octet-stream')? 'other' : type
  end
end
