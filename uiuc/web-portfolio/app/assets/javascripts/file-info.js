/**
 * Created by Alvin
 * Logic for showing the file info trees.
 */

function showFile(file){
    $('.file-node').css('display', 'none');
    $('#' + file).css('display', 'block');
    $('.rev-info > ul').children().find('li').hide();
}
