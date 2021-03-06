/**
 * Written by Jeromy French.
 * Bootstrap-themed tree, find it at https://github.com/jhfrench/bootstrap-tree
 * Minor changes by Alvin Ling
 */

$(document).ready(function() {
	$('.tree > ul').attr('role', 'tree').find('ul').attr('role', 'group');
	$('.tree').find('li:has(ul)').addClass('parent_li').attr('role', 'treeitem').find(' > span').attr('title', 'Collapse this branch').on('click', function (e) {
        var children = $(this).parent('li.parent_li').find(' > ul > li');
        if (children.is(':visible')) {
    		children.hide('fast');
    		$(this).attr('title', 'Expand this branch').find(' > i');
        }
        else {
    		children.show('fast');
    		$(this).attr('title', 'Collapse this branch').find(' > i');
        }
        e.stopPropagation();
    });
    $('.tree > ul').children().find('li').hide();
});