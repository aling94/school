require 'test_helper'
require 'nokogiri'
require_relative '../../app/models/projects/file_item'
require_relative '../../app/models/projects/dir_item'

class DirItemTest < ActiveSupport::TestCase

  SVN_LS = File.open('test/fixtures/test_list.xml') { |f| Nokogiri::XML(f) }.xpath('//entry')

  def setup
    @dirs = {}
    @files = {}

    SVN_LS.each do |entry|
      item = (entry['kind'] == 'dir')? DirItem.new(entry) : FileItem.new(entry)
      dict = (entry['kind'] == 'dir')? @dirs : @files
      dict[item.full_path] = item
      @dirs[item.parent].add_child(item) if @dirs.key?(item.parent)
    end

    @d1 = @dirs['Dir1']
    @d2 = @dirs['Dir2']
    @d3 = @dirs['Dir1/Dir3']
    @d4 = @dirs['Dir1/Dir4']

  end

  test "directories are present" do
    assert_not_nil(@d1)
    assert_not_nil(@d2)
    assert_not_nil(@d3)
    assert_not_nil(@d4)
  end

  test "constructors" do
    assert_equal('Dir1', @d1.name)
    assert_equal('', @d1.parent)
    assert_equal('Dir1', @d1.full_path)
    assert_equal('Dir2', @d2.name)
    assert_equal('Dir2', @d2.full_path)
    assert_equal('', @d2.parent)
    assert_equal('Dir3', @d3.name)
    assert_equal('Dir1', @d3.parent)
    assert_equal('Dir1/Dir3', @d3.full_path)
  end

  test "correct revision num" do
    assert_equal(1, @d1.info[:rev])
    assert_equal(2, @d2.info[:rev])
    assert_equal(3, @d3.info[:rev])
    assert_equal(11, @d4.info[:rev])
  end

  test "correct number of children" do
    assert_equal(2, @d1.children.size)
    assert_equal(1, @d2.children.size)
    assert_equal(1, @d3.children.size)
    assert_equal(0, @d4.children.size)
  end

  test "children added to correct dir" do
    assert @d1.children.include?(@d3)
    assert @d1.children.include?(@d4)
    assert @d2.children.include?(@files['Dir2/f2.java'])
    assert @d3.children.include?(@files['Dir1/Dir3/f1.txt'])
  end

end
