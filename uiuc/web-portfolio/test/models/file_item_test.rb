require 'test_helper'
require 'nokogiri'
require_relative '../../app/models/projects/file_item'

class FileItemTest < ActiveSupport::TestCase

  SVN_LS = File.open('test/fixtures/test_list.xml') { |f| Nokogiri::XML(f) }
  SVN_LG = File.open('test/fixtures/test_log.xml') { |f| Nokogiri::XML(f) }.xpath("//logentry")

  # Loads in files from the lists, then adds revisions from the logs
  def setup
    @files = []
    SVN_LS.xpath("//entry[@kind='file']").each {|entry| @files << FileItem.new(entry)}

    f1 = @files[0]
    f2 = @files[1]
    SVN_LG.each do |entry|
      entry.xpath("./paths/path[@kind='file']").each do |path|
        filename = path.text.gsub(/\s+/, '')
        filename = (filename.split('/').drop(2)).join('/')
        f1.add_rev(entry) if f1.full_path == filename
        f2.add_rev(entry) if f2.full_path == filename
      end
    end
  end

  # Loads in nodes from the XML file and passes them to the constructor. Checks name fields initialize correctly.
  test 'file_constructor' do
    assert_equal(2, @files.size)
    assert_equal('f1.txt', @files[0].name)
    assert_equal('Dir1/Dir3', @files[0].parent)
    assert_equal('Dir1/Dir3/f1.txt', @files[0].full_path)
    assert_equal('f2.java', @files[1].name)
    assert_equal('Dir2', @files[1].parent)
    assert_equal('Dir2/f2.java', @files[1].full_path)
  end

  # Checks the the number of revisions is correct and the revision numbers they contain are correct
  test 'revisions counts' do
    f1 = @files[0]
    f2 = @files[1]
    assert_equal(4, f1.revs.size)
    assert_equal(3, f2.revs.size)
    assert_equal([1,2,3,5], f1.revs.keys.sort)
    assert_equal([1,4,5], f2.revs.keys.sort)
  end

  # Verifies f1's revision contents
  test 'f1 revisions contents' do
    f1 = @files[0]
    assert_equal(rev_info('alvin', '2016-01-01', '1 alvin both'), f1.revs[1])
    assert_equal(rev_info('simon', '2016-01-02', '2 simon f1'), f1.revs[2])
    assert_equal(rev_info('simon', '2016-01-03', '3 simon f1'), f1.revs[3])
    assert_equal(rev_info('pablo', '2016-01-05', '5 pablo both'), f1.revs[5])
  end

  # Verifies f2's revision contents
  test 'f2 revisions contents' do
    f2 = @files[1]
    assert_equal(rev_info('alvin', '2016-01-01', '1 alvin both'), f2.revs[1])
    assert_equal(rev_info('theo', '2016-01-04', '4 theo f2'), f2.revs[4])
    assert_equal(rev_info('pablo', '2016-01-05', '5 pablo both'), f2.revs[5])
  end

  private

  # Helper for constructing a revision info Hash
  def rev_info(a, d, m)
    {author: a, date: d, msg: m}
  end
end
