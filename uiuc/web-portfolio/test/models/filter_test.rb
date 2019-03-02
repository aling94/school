require 'test_helper'

# Tests the filtering function of the Filter record.

class FilterTest < ActiveSupport::TestCase

  # Try a clean sentence. Nothing should happen
  test "filter clean sentence" do
    str = 'hello world rainbows kittens'
    filtered = Filter.filter_profanity(str)
    assert_equal(str, filtered)
    str2 = 'Hello World Rainbows Kittens'
    filtered2 = Filter.filter_profanity(str2)
    assert_equal(str2, filtered2)
  end

  # Try a dirty sentence. Sentence should have flags substituted
  test "filter dirty sentence" do
    str = 'flag1 hello world flag2 kittens'
    filtered = Filter.filter_profanity(str)
    assert_equal('sub1 hello world sub2 kittens', filtered)
  end

  # Try hiding flags next to words. Sentence should have flags substituted
  test "filter sneaky sentence" do
    str = 'flag1hello worldflag2 kittens'
    filtered = Filter.filter_profanity(str)
    assert_equal('sub1hello worldsub2 kittens', filtered)
  end

  # Try hiding flags next to words with caps. Sentence should have flags substituted
  test "filter caps dirty sentence" do
    str = 'FLAG1hello worldFLAG2 kittens'
    filtered = Filter.filter_profanity(str)
    assert_equal('sub1hello worldsub2 kittens', filtered)
  end

  # Try hiding flags with mixed case. Sentence should have flags substituted
  test "filter mixed dirty sentence" do
    str = 'FlAg1hello worldFlaG2 kittens'
    filtered = Filter.filter_profanity(str)
    assert_equal('sub1hello worldsub2 kittens', filtered)
  end

end
