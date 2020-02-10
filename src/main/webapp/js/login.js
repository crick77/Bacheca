// JavaScript Document
//login
function login (){
	
	var username = (document.getElementById("exampleInputEmail")).value;
	var password = (document.getElementById("exampleInputPassword")).value;
	
	$.ajax({ 
             type: "POST",
             contentType: "application/json",
             url: "http://localhost:8080/Bacheca/api/auth/login",
			 data: '{"username": "'+username+'","password": "'+password+'"}',
             success: function(dati){
				window.localStorage.setItem('username', username);
				window.localStorage.setItem('code', dati);
			    window.location = "index.html";
             },
			error: function(){
				$('#informationBar').show('#informationBar');
			}
		
         });
}

