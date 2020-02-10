// JavaScript Document
//view allegati caricati 
function caricaAllegati(){
		        document.getElementById('fileAllegati').innerHTML = "<span>File caricati:</span>";
		        var name = document.getElementById('allegati'); 
		        for (var i=0; i< name.files.length;i++)
                	$('#fileAllegati').append('<br>'+name.files.item(i).name);
			}
//view coumento caricato 
function caricaDocumento(){
		        document.getElementById('fileDocumento').innerHTML = "<span>File caricati:</span>";
		        var name = document.getElementById('documento'); 
		        $('#fileDocumento').append('<br>'+name.files.item(0).name);
			}

//gestione invio form
$('form').submit(function (e) {
    var form;
	form = new FormData();
    form.append('documento', $('#documento')[0].files[0]);
	var name = document.getElementById('allegati'); 
	for (var i=0; i< name.files.length;i++)
                	form.append('allegato', $('#allegati')[0].files[i]);
	e.preventDefault();
//	console.log (data.getAll('documento'));
    $.ajax({
        url: "http://localhost:8080/Bacheca/api/board/publish?tipo="+$('#tipo').val()+"&numero="+$('#numero').val()+"&datapubblicazione="+$('#datapubblicazione').val()+"&titolo="+$('#titolo').val()+"&ufficio="+$('#ufficio').val()+"&proprietario="+window.localStorage.getItem('username')+"",
  		method: "POST",
  		timeout: 0,
  		headers: {
			"Authorization": "Bearer "+window.localStorage.getItem('code')
  					},
  		processData: false,
		contentType: false,
  		data: form,
		statusCode: {
      		409: function (response) {
         	              $('#conflict').show('#conflict');
      	},
			401: function (response) {
         	              window.location = "index.html";
      	}
		},
		success: function(data){
			window.location = "index.html";
		},
    });
});
