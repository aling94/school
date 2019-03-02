# Represents a filter, with the flagged-words and substitutions being backed by a database.

class Filter < ActiveRecord::Base

  # Static function for filtering a string. Returns a new, filtered copy of the string.
  def self.filter_profanity(str)
    return '' unless str
    words = str.split(' ')
    return '' if words.empty?
    max_len = words.max_by(&:length).length
    filters = Filter.where('length(flag) <= ?', max_len).map{|f| [f.flag, f.sub]}
    clean_str(str, filters)
  end

  private
  # Helper for filtering a string.
  def self.clean_str(str, filters)
    copy = str.dup
    filters.each do |flag,sub|
      copy.gsub!(/#{flag}/i, sub)
    end
    copy
  end
end
