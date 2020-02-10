// javascript for ODG 

function getODG() {
 let searchParams = new URLSearchParams(window.location.search);
 let year = searchParams.get('year');
 let filter = searchParams.get('q');
 if (filter){
	 $('#filtro').val(filter);
	 filter = "?q="+filter;
 }
 else{
	 filter = "";
 }
	console.log("selected year="+year);
 if (year)
	$.ajax({ 
             type: "GET",
             contentType: "application/json",
             url: "http://localhost:8080/Bacheca/api/board/"+year+"/ODG"+filter,
             success: function(dati){
            	 console.log("Dati="+dati);
				var x = window.localStorage.getItem('code');
				if (x) //if logged
				  {
					  for(let i = 0; i<dati.length; i++)
				  		{
						  console.log("Dato="+dati[i]);
						    x = '<tr><td>' + dati[i].numero + '</td><td>' + dati[i].dataPubblicazione + '</td><td>' + dati[i].titolo + '</td>';
						 	x += '<td>' + dati[i].ufficio+ '</td>';
						 	x += '<td>' + dati[i].proprietario + '</td>';
						 	x += '<td><a class="btn btn-success btn-circle btn-sm" href="http://localhost:8080/Bacheca/api/board/ODG/'+dati[i].id+'/stream" download><i class="fas fa-download"></i></a></td><td><a class="btn btn-success btn-circle btn-sm" href=# onclick="startModal(\'ODG/'+dati[i].id+'/attachments\')"><i class="fas fa-paperclip"></i></a></td>';			
						 	x += '<td><i class="fa '+text2icon(dati[i].mailStatus)+'"></td>';
						 	x += '<td><i class="fa '+text2icon(dati[i].indexingStatus)+'"></td>';
						 	x += '<td><a onclick="deleteOdg('+dati[i].id+')" href="#" class="btn btn-danger btn-circle"><i class="fas fa-trash"></i></a></td></tr>';
						 	$('#odgData').append($(x));
					  	} 
				  }
				 else
					 {
						for(let i = 0; i<dati.length; i++)
				  			{
							console.log("Dato="+dati[i]);
							x = '<tr><td>' + dati[i].numero + '</td><td>' + dati[i].dataPubblicazione + '</td><td>' + dati[i].titolo + '</td>';
					 		x += '<td>' + dati[i].ufficio+ '</td>';
						 	x += '<td>' + dati[i].proprietario + '</td>';
					 		x += '</td><td><a class="btn btn-success btn-circle btn-sm" href="http://localhost:8080/Bacheca/api/board/ODG/'+dati[i].id+'/stream" download><i class="fas fa-download"></i></a></td><td><a class="btn btn-success btn-circle btn-sm" href=# onclick="startModal(\'ODG/'+dati[i].id+'/attachments\')"><i class="fas fa-paperclip"></i></a></td>';
					 		x += '<td><i class="fa '+text2icon(dati[i].mailStatus)+'"></td>';
						 	x += '<td><i class="fa '+text2icon(dati[i].indexingStatus)+'"></td>';
					 		$('#odgData').append($(x));
				  			} 
					 }
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
	else
		$.ajax({ 
             type: "GET",
             contentType: "application/json",
             url: "http://localhost:8080/Bacheca/api/board/ODG"+filter,
             success: function(dati){
            	console.log("No year, dati="+dati);
				var x = window.localStorage.getItem('code');
				if (x)//if logged
				  {
					  for(let i = 0; i<dati.length; i++)
				  		{
					 	x = '<tr><td>' + dati[i].numero + '</td><td>' + dati[i].dataPubblicazione + '</td><td>' + dati[i].titolo + '</td>';
					 	x += '<td>' + dati[i].ufficio+ '</td>';
					 	x += '<td>' + dati[i].proprietario + '</td>';
					 	x += '<td><a class="btn btn-success btn-circle btn-sm" href="http://localhost:8080/Bacheca/api/board/ODG/'+dati[i].id+'/stream" download><i class="fas fa-download"></i></a></td><td><a class="btn btn-success btn-circle btn-sm" href=# onclick="startModal(\'ODG/'+dati[i].id+'/attachments\')"><i class="fas fa-paperclip"></i></a></td>';			
					 	x += '<td><i class="fa '+text2icon(dati[i].mailStatus)+'"></td>';
					 	x += '<td><i class="fa '+text2icon(dati[i].indexingStatus)+'"></td>';
					 	x += '<td><a onclick="deleteOdg('+dati[i].id+')" href="#" class="btn btn-danger btn-circle"><i class="fas fa-trash"></i></a></td></tr>';					 	
					 	$('#odgData').append($(x));
				  		} 
				  }
				 else
					 {
						for(let i = 0; i<dati.length; i++)
				  			{
					 		x = '<tr><td>' + dati[i].numero + '</td><td>' + dati[i].dataPubblicazione + '</td><td>' + dati[i].titolo + '</td>';
					 		x += '<td>' + dati[i].ufficio+ '</td>';
						 	x += '<td>' + dati[i].proprietario + '</td>';
					 		x += '</td><td><a class="btn btn-success btn-circle btn-sm" href="http://localhost:8080/Bacheca/api/board/ODG/'+dati[i].id+'/stream" download><i class="fas fa-download"></i></a></td><td><a class="btn btn-success btn-circle btn-sm" href=# onclick="startModal(\'ODG/'+dati[i].id+'/attachments\')"><i class="fas fa-paperclip"></i></a></td>';
					 		x += '<td><i class="fa '+text2icon(dati[i].mailStatus)+'"/></td>';
						 	x += '<td><i class="fa '+text2icon(dati[i].indexingStatus)+'"/></td>';
					 		$('#odgData').append($(x));
				  			} 
					 }
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

getODG();

//Delete a row ODG
function deleteOdg(id){
	if(!window.confirm("Cancellare la pubblicazione selezionata?")) return;
	
	let tokenId = window.localStorage.getItem('code');
	$.ajax({ 
             type: "DELETE",
             contentType: "application/json",
			 headers :{
				 "Authorization": 'Bearer '+tokenId
			 },
             url: "http://localhost:8080/Bacheca/api/board/ODG/"+id,
             success: function(dati){
				location.reload();
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
//Delete a row ODG per anno
function deleteOdgYear(id, year){
	if(!window.confirm("Cancellare la pubblicazione selezionata?")) return;
	
	let tokenId = window.localStorage.getItem('code');
	$.ajax({ 
             type: "DELETE",
             contentType: "application/json",
			 headers :{
				 "Authorization": 'Bearer '+tokenId
			 },
             url: "http://localhost:8080/Bacheca/api/board/"+year+"/ODG/"+id,
             success: function(dati){
				location.reload();
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

function startModal(allegati){

	$.ajax({
		type: "GET",
             contentType: "application/json",
             url: "http://localhost:8080/Bacheca/api/board/"+allegati,
             success: function(dati){
				 $('#allegatiData').empty();
				 if(dati.length>0) 
				     for(let i = dati.length-1; i >=0; i--)
				      			{
					     			x = '<tr><td>' + dati[i].nomefile + '</td><td><a class="btn btn-success btn-circle btn-sm" href="http://localhost:8080/Bacheca/api/board/'+allegati+'/' + dati[i].id +'/stream"><i class="fas fa-download"></i></a></td>';
					     			$('#allegatiData').append($(x));
				      			} 
				 else
					 $('#allegatiData').append($('<td>Nessun allegato</td>'));
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
	$('#allegati').modal();
}

function search(){
	let searchParams = new URLSearchParams(window.location.search);
	let year = searchParams.get('year');
	if (year)
		window.location.replace('odg.html?year='+year+'&q='+$('#filtro').val());
	else
		window.location.replace('odg.html?q='+$('#filtro').val());
}

function text2icon(text) {
	if(text=='PENDING') return 'fa-clock';
	if(text=='OK') return 'fa-check';
	if(text=='FAILED') return 'fa-times';
	return 'fa-question';
}

