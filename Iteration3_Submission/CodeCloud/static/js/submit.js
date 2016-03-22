 var text="";
     function submission(){
             for(i=0;i<document.choose.assignmentsubmit.assignment.length;i++){
              if(document.choose.assignmentsubmit.assignment[i].checked==true){
                   text=text+","+document.choose.assignmentsubmit.assignment[i].value;
              }
             }
             var txt = document.getElementById("submit");
             txt.innerHTML=text;
       } 