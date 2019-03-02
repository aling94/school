require 'test_helper'

# Tests the post's validation. Comments are essentially the same.

class PostTest < ActiveSupport::TestCase

  # Try empty inputs
  test "empty post won't save" do
    empty = Post.new
    assert_not(empty.save)
    spaces = Post.new({name: '      ', body: '          '})
    assert_not(spaces.save)
    name_missing = Post.new({body: 'body'})
    assert_not(name_missing.save)
    body_missing = Post.new({name: 'name'})
    assert_not(body_missing.save)
  end

  # Try sticking non letters in name
  test "non letters in name" do
    post = Post.new({name: 'Alvin; DROP posts', body: 'test post won\'t should succeed'})
    assert_not(post.save)
    post = Post.new({name: 'Alvin)--', body: 'neither will this'})
    assert_not(post.save)
    post = Post.new({name: 'test1,\'test2\')-- -', body: 'neither will this'})
    assert_not(post.save)
  end

  # Normal input, should be successful
  test "successful save normal text" do
    post = Post.new({name: 'Alvin', body: 'test post should succeed'})
    assert(post.save)
    assert_equal(post, Post.where(name: 'Alvin').first)
  end

  # Try a query with some hidden SQL code in a WHERE clause
  test "SQL injection query" do
    post = Post.new({name: 'Alvin', body: 'test post should succeed'})
    assert(post.save)
    Post.where(name: 'Alvin; DROP posts')
    assert_equal(post, Post.where(name: 'Alvin').first)
  end

  # Try an INSERT with some SQL code. Would normally cause error, but should insert without problems
  test "SQL injection insert quotes" do
    post = Post.new({name: 'Alvin', body: "''"})
    assert(post.save)
    Post.where(name: 'Alvin')
    assert_equal("''", Post.where(name: 'Alvin').first.body)
  end

  # Try an INSERT with some SQL comments.
  # Would normally insert nothing and print DB, but should insert without problems.
  test "SQL injection insert comments" do
    post = Post.new({name: 'Alvin', body: ')--'})
    assert(post.save)
    Post.where(name: 'Alvin')
    assert_equal(')--', Post.where(name: 'Alvin').first.body)
  end

  # Try to drop the whole table with some hidden commands. Table should be intact
  test "SQL injection commands" do
    post = Post.new({name: 'Alvin', body: 'hello); DROP posts;'})
    assert(post.save)
    result = Post.where(name: 'Alvin').first
    assert_not_nil(result)
    assert_equal('hello); DROP posts;', result.body)
  end

  # Try HTML and JS inputs. Should successfully save sanitized versions of the input
  test "HTML/JS escaping" do
    bpost = Post.new({name: 'Alvin', body: '<b>'})
    assert(bpost.save)
    assert_equal('<b></b>', Post.where(name: 'Alvin').first.body)
    script_post = Post.new({name: 'Pablo', body: '<script>console.log(\'Get rekt.\');</script>'})
    assert(script_post.save)
    assert_equal('console.log(\'Get rekt.\');', Post.where(name: 'Pablo').first.body)
    mixed_post = Post.new({name: 'Theo', body: '<b>Test</script>'})
    assert(mixed_post.save)
    assert_equal('<b>Test</b>', Post.where(name: 'Theo').first.body)
  end

  # Try profane inputs. Bad names should be rejected. Bad body text should be substituted.
  # More filtering tested in actual Filter model test
  test "Profanity validation" do
    post = Post.new({name: 'flag1', body: '<b>'})
    assert_not(post.save)
    post2 = Post.new({name: 'Alvin', body: 'flag1'})
    assert(post2.save)
    assert_equal('sub1', Post.where(name: 'Alvin').first.body)
  end

end
