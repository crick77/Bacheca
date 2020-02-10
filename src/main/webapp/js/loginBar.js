// javascript for loginBar

//javascript show log bar
function start()
{
	let x = window.localStorage.getItem('code');
	if (x){
	$("#login").show("#login");
	$('#logged').hide('#logged');
	}
	else{
		$("#login").hide("#login");
		$('#logged').show('#logged');
	}
}
start();

// javascript counter
function counter(){
	$.ajax({ 
             type: "GET",
             contentType: "application/json",
             url: "http://localhost:8080/Bacheca/api/auth/count",
             success: function(dati){
				$("#loggedCount").text(dati);
             }
         });
}

counter();

// javascript for logout
function logout(){
	let tokenId = window.localStorage.getItem('code');
	window.alert(tokenId);
	$.ajax({
             type: "POST",
		     headers :{
				 "Authorization": 'Bearer '+tokenId
			 },
		     url: "http://localhost:8080/Bacheca/api/auth/logout",
             success: function(dati){
				
				 window.localStorage.removeItem('code');
				 location.reload();
             },
			error: function(){
				 console.log('it can not logout');
			}

         });
}

//javascript per yaer

function getYear(){
	console.log("YEARS!");
	$.ajax({
             type: "GET",
			 contentType: "application/json",
		     url: "http://localhost:8080/Bacheca/api/board/DDS/years",
             success: function(dati){
			 for(let i=0; i<dati.length; i++ ){
				 console.log("YEAR="+dati[i]);
				 x = '<a class="collapse-item" href="dds.html?year='+dati[i]+'">'+dati[i]+'</a>';
				 console.log($('#ancDds'));
				 $('#ancDds').append($(x));
			 }
			 }
         });
	$.ajax({
             type: "GET",
			 contentType: "application/json",
		     url: "http://localhost:8080/Bacheca/api/board/ODG/years",
             success: function(dati){
			 for(let i=0; i<dati.length; i++ ){
				 console.log("YEAR="+dati[i]);
				 x = '<a class="collapse-item" href="odg.html?year='+dati[i]+'">'+dati[i]+'</a>';
				 console.log($('#ancOdg'));
				 $('#ancOdg').append($(x));
			 }
			 }
         });
}
getYear();

function reindex() {
	$.ajax({
		type: "GET",
             contentType: "application/json",
             url: "http://localhost:8080/Bacheca/api/board/reindex",
             headers: {
     			"Authorization": "Bearer "+window.localStorage.getItem('code')
             },
             success: function(dati){
            	 window.alert("Reindicizzazione avviata. L'operazione potrebbe richiedere tempo...");
             },
			 error: function (xhr, ajaxOptions, thrownError) {
		        switch(xhr.status) {
			        case 401: {
			        	window.alert("Non si è autorizzati ad eseguire questa operazione.");
			        	window.location = "index.html";
			        	break;
			        }
			        case 404: {
			        	window.alert("Nessuna informazione trovata!");
			        	break;
			        }
			        case 409: {
			        	window.alert("Informazione duplicata!");
			        	break;
			        }
			        default: {
			        	window.alert(thrownError);
			        }
		        }		      
		      }
	});
}
