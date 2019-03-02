/**
 * Created by Alvin on 3/17/2016.
 * Logic for displaying comment and reply forms.
 */


$(document).ready(function () {

    // Logic for showing and hiding reply forms
    $('.reply-form').html('');
    $("button.reply").click(function () {
        var pid = $(this).val();
        var formdiv = $('#replyto' + pid);
        var me = $(this);
        if(formdiv.html().length == 0)
        {
            $('.reply-form').html('');
            formdiv.html($('.base-reply').html());
            formdiv.find('#postid').val(pid);
        } else
        {
            $('.reply-form').html('');
        }
    });

    // Show and hide reply lists
    $('.replies > ul > li').hide();
    $('button.show-reply').each(function() {
        var me = $(this);
        var replies = me.parent().parent().find('.replies > ul li');
        me.click(function() {
            if(me.is('.open'))
            {
                me.html('Show Replies');
                me.removeClass('open');
                replies.hide();
            } else
            {
                me.html('Hide Replies');
                me.addClass('open');
                replies.show();
            }
        });
    });
});
