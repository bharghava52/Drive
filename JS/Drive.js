document.addEventListener('contextmenu', event => event.preventDefault());
var users=[];

$(document).ready(function (){

    $.post("GetSession",{},
    function(data,status){
        obj=JSON.parse(data);
        $("#id").append(obj.User);
    });

    $.post("Users",{},function(data){
        users=$.parseJSON(data);
        var list="";
        for(var i=0;i<users.length;i++){
          list+="<li style='display:none'><a href='#' class='shareinput'>"+users[i]+"</a></li>";  
        }
        document.getElementById("users").innerHTML=list;
    });

    $(document).on('click','.shareinput',function(){
        $("#sharemail").val($(this).text());
    });

    $.post("GetFiles",{},function(data,status){
        $("#files").append(data);
        $.post("SharedFiles",{},function(data){
                $("#files").append(data);
        });
    });

    $(document).on('contextmenu','.directory',function(e){
        if($(this).text()!="<--Back"){
            e.preventDefault();
            $("#foldermenu").css("left",e.pageX);
            $("#foldermenu").css("top",e.pageY);     
            $("#foldermenu").fadeIn(200,startFocusOut());
            $("#foldermenupath").val($(this).attr("path"));
            $("#foldermenupath").attr("file",$(this).text());
        }
    });

    $(document).on('contextmenu','.file',function(e){
        e.preventDefault();
        $("#filemenu").css("left",e.pageX);
        $("#filemenu").css("top",e.pageY);     
        $("#filemenu").fadeIn(200,startFocusOut());
        $("#filemenupath").val($(this).attr("path"));
    });

    $(document).on('contextmenu','.folderSHARE',function(e){
        if($(this).text()!="<--Back"){
            e.preventDefault();
            $("#foldermenu").css("left",e.pageX);
            $("#foldermenu").css("top",e.pageY);     
            $("#foldermenu").fadeIn(200,startFocusOut());
            $("#foldermenupath").val($(this).attr("path"));
            $("#foldermenupath").attr("file",$(this).text());
        }
    });

    $(document).on('contextmenu','.fileSHARE',function(e){
        e.preventDefault();
        $("#filemenu").css("left",e.pageX);
        $("#filemenu").css("top",e.pageY);     
        $("#filemenu").fadeIn(200,startFocusOut());
        $("#filemenupath").val($(this).attr("path"));
    });

    $(document).on('contextmenu','.folderEDIT',function(e){
        if($(this).text()!="<--Back"){
            e.preventDefault();
            $("#folderEDIT").css("left",e.pageX);
            $("#folderEDIT").css("top",e.pageY);     
            $("#folderEDIT").fadeIn(200,startFocusOut());
            $("#folderEDITpath").val($(this).attr("path"));
            $("#folderEDITpath").attr("file",$(this).text());
        }
    });

    $(document).on('contextmenu','.fileEDIT',function(e){
        e.preventDefault();
        $("#fileEDIT").css("left",e.pageX);
        $("#fileEDIT").css("top",e.pageY);     
        $("#fileEDIT").fadeIn(200,startFocusOut());
        $("#fileEDITpath").val($(this).attr("path"));
    });

    $(document).on('click','.directory',function(){
        $.post("LocFiles",
            {
                    path:$(this).attr('path')
            },
            function(data,status,xhr){
                $("#files").empty();
                $("#files").append(data);
                if($(this).attr('path')===$("#id").text()){ 
                    $.post("SharedFiles",{},function(data1){
                          $("#files").append(data1);
                    });  
                }
            }
        );
    });

    $(document).on('click','.frienddatas',function(){
        $.post("FriendDatas",{
            root:$("#root").val(),
            path:$(this).attr('path')
        },
        function(data){
            $("#files").empty();
            $("#files").append(data);
        });
    });

    $(document).on('click','#createfolder',function(){
        var check=$("#fldrname").val();
        var n=check.search("\\.");
        if(n==-1){
            $.post("CreateFolder",
                {
                    path:$("#path").val(),
                    name:$("#fldrname").val()
                },
                function(data,status){
                    alert(data);
                    $.post("LocFiles",
                        {
                            path:$("#path").val()
                        },
                        function(data,status,xhr){
                            $("#files").empty();
                            $("#files").append(data);
                        }
                    );
                    var folderBox1 = document.getElementById("folderBox");
                    folderBox1.style.display="none";
                    if(data=="this folder already exists:"){
                        alert(data);
                    }    
                    var fldrname = document.getElementById("fldrname");
                    fldrname.value="";
                }
            );

        }else{
            alert("folder cannot contain dot '.' :"+n);
        }   
    });   

    $('.upload-btn').on('click', function (){
        $('#upload-input').click();
        $('.progress-bar').text('0%');
        $('.progress-bar').width('0%');
    });

    $(document).on('click','#snackbarclose',function(){
       $.post("LocFiles",
            {
                path:$("#path").val()
            },
            function(data,status,xhr){
                $("#files").empty();
                $("#files").append(data);
            }
        ); 
    });

    $(document).on('click','.sharedwithme',function(){
        var x=document.getElementById("myBtn");
        var ubt=document.getElementById("uploadBtn");
        var cfbt=document.getElementById("folderBtn");
        x.style.display="none";
        ubt.style.display="none";
        cfbt.style.display="none";
        $.post("SharedFiles",{},
        function(data){
            $("#files").empty();
            $("#files").append(data);
        });
    });

    $(document).on('click','.friend',function(){
       var x=document.getElementById("myBtn");
       var ubt=document.getElementById("uploadBtn");
       var cfbt=document.getElementById("folderBtn");
       x.style.display="none";
       ubt.style.display="none";
       cfbt.style.display="none";
       $.post("FriendFiles",{
           friend:$(this).attr('user')
       },function(data){
           $("#files").empty();
           $("#files").append(data);
       }); 
    });


    $(document).on('click','.frienddata',function(){
        $.post("FriendData",{
            path:$(this).attr('path')
        },function(data,status){
            $("#files").empty();
            $("#files").append(data);
        });    
    });

    $(document).on('click','.folderEDIT',function(){
        var x=document.getElementById("myBtn");
        x.style.display="block";
    });


    $(document).on('click','.folderSHARE',function(){
        var x=document.getElementById("myBtn");
        x.style.display="block";
    });

    $(document).on('click','#mydrive',function(){
        var x=document.getElementById("myBtn");
        x.style.display="block";
        $.post("GetFiles",{},function(data){
            $("#files").empty();
            $("#files").append(data);
            $.post("SharedFiles",{},function(data1){
                $("#files").append(data1);
            });
        }); 

    });

    $(document).on('click','#id',function(){
        var x=document.getElementById("myBtn");
        x.style.display="block";
        $.post("GetFiles",{},function(data){
            $("#files").empty();
            $("#files").append(data);
            $.post("SharedFiles",{},function(data1){
                $("#files").append(data1);
            });
        }); 

    });

    $("#folderitems > li.download").click(function(){
        $.post("DownloadFolder",{
            path:$("#foldermenupath").val(),
            file:$("#foldermenupath").attr("file")
        },function(data,status){
            window.location="DownloadFile?path="+$("#foldermenupath").val()+".zip"+"&file="+$("#foldermenupath").attr("file")+".zip";
            $.post("DeleteFolder",{
                path:$("#foldermenupath").val()+".zip"
            },function(data){
                alert(data);
            });
        });
    });


    $("#folderitems > li.delete").click(function(){
        if(confirm("Are you sure to delete this folder")){
            $.post("DeleteFolder",{
                path:$("#foldermenupath").val()
            },function(data){
                alert(data); 
            });
        }   
    });

    $("#fileitems > li.delete").click(function(){
        if(confirm("Are you sure to delete this file")){
            $.post("DeleteFolder",{
                path:$("#filemenupath").val()
            },function(data){
                alert(data);
            });
        }   
    });

    $("#folderitems > li.share").click(function(){
        $("#sharepath").val($("#foldermenupath").val());
        showshareBox();
    });

    $("#fileitems > li.share").click(function(){
        $("#sharepath").val($("#filemenupath").val());
        showshareBox();
    });

    $("#folderEDITitems > li.download").click(function(){
        $.post("DownloadFolder",{
            path:$("#folderEDITpath").val(),
            file:$("#folderEDITpath").attr("file")
        },function(data,status){
            window.location="DownloadFile?path="+$("#folderEDITpath").val()+".zip"+"&file="+$("#folderEDITpath").attr("file")+".zip";
            $.post("DeleteFolder",{
                path:$("#folderEDITpath").val()+".zip"
            },function(data){
                alert(data);
            });
        });
    });


    $("#folderEDITitems > li.delete").click(function(){
        if(confirm("Are you sure to delete this folder")){
            $.post("DeleteFolder",{
                path:$("#folderEDITpath").val()
            },function(data){
                alert(data); 
            });
        }   
    });

    $("#fileEDITitems > li.delete").click(function(){
        if(confirm("Are you sure to delete this file")){
            $.post("DeleteFolder",{
                path:$("#fileEDITpath").val()
            },function(data){
                alert(data); 
            });
        }   
    });

    $(document).on('click',"#sharetomail",function(){
        alert($("#sharetype").val());
        $.post('Share',{
            mail:$("#sharemail").val(),
            path:$("#sharepath").val(),
            type:$("#sharetype").val()
        },function(data){
            alert(data);
        });
    });

});

function toggle(){
    var ubt=document.getElementById("uploadBtn");
    var cfbt=document.getElementById("folderBtn");
    if(ubt.style.display==="block"){
        ubt.style.display="none";
        cfbt.style.display="none";
    }
    else{
        ubt.style.display="block";
        cfbt.style.display="block";
    }
}

function uploadFunction() {
    var x = document.getElementById("snackbar");
    var x1=document.getElementById("path");
    var x2=document.getElementById("uploadpath");
    x2.setAttribute("value",x1.getAttribute("value").toString());
    x.className = "show";
    x.style.visibility="visible";
    //setTimeout(function(){ x.className = x.className.replace("show", ""); }, 10000);
}

function showfolderBox(){
    var folderBox1 = document.getElementById("folderBox");
    folderBox1.style.display="block";
}
function closefolderBox(){
    var folderBox1 = document.getElementById("folderBox");
    var fldrname=document.getElementById("fldrname");
    fldrname.value="";
    folderBox1.style.display="none";
}

function closesnackbar(){
    var snackbar=document.getElementById("snackbar");
    snackbar.style.visibility="hidden";
}

function startFocusOut(){
    $(document).on("click",function(){
        $("#foldermenu").hide();
        $("#filemenu").hide();
        $("#folderEDIT").hide();
        $("#fileEDIT").hide();
    });
}

function showshareBox(){
    var folderBox1 = document.getElementById("shareBox");
    folderBox1.style.display="block";
}

function closeshareBox(){
    var folderBox1 = document.getElementById("shareBox");
    var sharemail =document.getElementById("sharemail");
    sharemail.value="";
    folderBox1.style.display="none";

}

function search() {
    var input, filter, ul, li, a, i;
    input = document.getElementById("sharemail");
    filter = input.value.toUpperCase();
    ul = document.getElementById("users");
    li = ul.getElementsByTagName("li");
    var count=0;
    for (i = 0; i < li.length; i++) {
        a = li[i].getElementsByTagName("a")[0];
        if (a.innerHTML.toUpperCase().indexOf(filter) > -1 && count<1) {
            li[i].style.display = "";
            count++;
        } else {
            li[i].style.display = "none";
        }
    }
}


