(function(){
    videos = document.querySelectorAll(".clip_wrapper");
    var vidList = [];
    var regUrl = "(.*)config";
    for (var i = 0; i < videos.length; i++){
        try{
    		var url = videos[i].querySelector(".player_container > .player").getAttribute("data-config-url").match(regUrl)[1];
            var title = videos[i].querySelector(".player_container > .player_thumb").getAttribute("alt");
            var img = videos[i].querySelector(".player_container > .player_thumb").getAttribute("src");
            vidItem = {
            	vidUrl : url,
    			imgUrl : img
    		}
    		vidList.push(vidItem);
            Android.addVideo(String(url), String(img), String(title))
        } catch(err){
        }
    }
    if(vidList.length > 0) {
        Android.showVimeoVideoActivity();
    } else {
        Android.showVimeoNoVideoDialog();
    }
})();