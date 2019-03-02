require_relative 'dir_item'
require_relative 'file_item'

# Sets up the directory tree and file list from the given svn list and log
class Projects

  def self.project_tree(svn_ls, svn_lg, desc=nil)
    svn_ls = svn_ls.xpath("//entry")
    svn_lg = svn_lg.xpath("//logentry")
    dirs = {}
    files = {}

    svn_ls.each do |entry|
      item = (entry['kind'] == 'dir')? DirItem.new(entry) : FileItem.new(entry)
      dict = (entry['kind'] == 'dir')? dirs : files
      dict[item.full_path] = item
      dirs[item.parent].add_child(item) if dirs.key?(item.parent)
    end

    svn_lg.each do |entry|
      entry.xpath("./paths/path[@kind='file']").each do |path|
        filename = path.text.gsub(/\s+/, '')
        filename = (filename.split('/').drop(2)).join('/')
        files[filename].add_rev(entry) if files.key?(filename)
      end
    end
    desc.each {|name, desc| dirs[name].info[:msg] = desc if dirs.key?(name)} if desc

    return dirs, files
  end

end