$(function(){
	var workers = ['미정','장현서','노정호','김주리','이재오'];
	
	var calId = 'icfhjbs5edplnlkdhscnc9l25c@group.calendar.google.com'; // 방송글로벌
	
	var scheduler = new RainScheduler.Scheduler("scheduler"), now = new Date(), startDate = new Date();
	startDate.setMonth(now.getMonth()-1);

    // behavior and appearance
    scheduler.theme = "scheduler_blue";

    // view
    scheduler.startDate = startDate;
    scheduler.cellGroupBy = "Month";
    scheduler.days = 210;
    scheduler.cellDuration = 1440; // 60 1시간
    scheduler.moveBy = 'Full';

    // bubble, sync loading
    // see also RainScheduler.Event.data.staticBubbleHTML property
    scheduler.bubble = new RainScheduler.Bubble({
        onLoad: function(args) {
            var ev = args.source;
            args.html = "testing bubble for: " + ev.text();
        }
    });
    
    scheduler.contextMenu = new RainScheduler.Menu({items: [
	    	{
	    		text:"summary 수정", 
	    		onclick: function() {
	    			var name = prompt("템플릿 => [프로젝트명] 작업내용");
	    			if(name){
	    				gapi.client.calendar.events.patch({
	    					calendarId: calId,
	    					eventId: this.source.id(),
	    					resource: {
	    						summary : name
	    					}
	    				}).execute(function(event) {
	    					location.reload();
	    				})
	    			}
	    		} 
	    	},
	    	{
	    		text:"description 수정", 
	    		onclick: function() {
	    			var description = prompt("설명");
	    			if(description){
	    				gapi.client.calendar.events.patch({
	    					calendarId: calId,
	    					eventId: this.source.id(),
	    					resource: {
	    						description : description
	    					}
	    				}).execute(function(event) {
	    					var e = scheduler.events.find(event.id);
	    					e.data['text']=event.description;
	    					scheduler.events.update(e);
	    				})
	    			}
	    		} 
	    	},
	    	{
	    		text:"작업자", items: [
	    			{text:workers[0], onclick: function(){ setWorker(this.source.id(),0); } },
	    			{text:workers[1], onclick: function(){ setWorker(this.source.id(),1); } },
                    {text:workers[2], onclick: function(){ setWorker(this.source.id(),2); } },
                    {text:workers[3], onclick: function(){ setWorker(this.source.id(),3); } },
                    {text:workers[4], onclick: function(){ setWorker(this.source.id(),4); } }
                ]
	    	},
	    	{
	    		text:"일정 삭제", 
	    		onclick: function() {
	    			if(confirm('삭제할까요?')){
	    				var id = this.source.id();
	    				gapi.client.calendar.events.delete({
	    					calendarId: calId,
	    					eventId: id,
	    				}).execute(function(event) {
	    					var e = scheduler.events.find(id);
	    					scheduler.events.remove(e);
	    				})
	    			}
	    		} 
	    	}
	    ]});
    
    function setWorker(id,colorId){
    	gapi.client.calendar.events.patch({
			calendarId: calId,
			eventId: id,
			resource: {
				colorId : colorId
			}
		}).execute(function(event) {
			//console.log(event);
			var e = scheduler.events.find(event.id);
			e.data['workerId']=event.colorId;
			scheduler.events.update(e);
		})
    }

    scheduler.treeEnabled = true;
    scheduler.rowHeaderWidth = 200;
    scheduler.eventHoverHandling = "Bubble";
    
    // event moving
    scheduler.onEventMoved = function (args) {
    	//console.log("arg:"+JSON.stringify(args));
        scheduler.message("Moved: " + args.e.text());
        
        
        gapi.client.calendar.events.patch({
        	calendarId: calId,
        	eventId: args.e.id(),
            resource: {
            	start:{  
            		date:args.newStart.toString().substring(0,10)
                 },
                 end:{  
                	 date:args.newEnd.toString().substring(0,10)
                  }
            }
        }).execute(function(event) {
            //appendPre('Event created: ' + event.htmlLink);
            scheduler.message('moved, ' + event.htmlLink);
        })
    };

    // event resizing
    scheduler.onEventResized = function (args) {
    	//console.log("arg:"+JSON.stringify(args));
        scheduler.message("Resized: " + args.e.text());
        
        
        gapi.client.calendar.events.patch({
        	calendarId: calId,
        	eventId: args.e.id(),
            resource: {
            	start:{  
            		date:args.newStart.toString().substring(0,10)
                 },
                 end:{  
                	 date:args.newEnd.toString().substring(0,10)
                  }
            }
        }).execute(function(event) {
            //appendPre('Event created: ' + event.htmlLink);
            scheduler.message('resized, ' + event.htmlLink);
        })
    };

    // event creating
    scheduler.onTimeRangeSelected = function (args) {
    	//console.log("arg:"+JSON.stringify(args));
        var name = prompt("템플릿 => [프로젝트명] 작업내용");
        var description = prompt("내용 ");
        if(name&&description){
        	gapi.client.calendar.events.insert({
        		calendarId: calId,
        		resource: {
        			summary : name,
        			start:{  
        				date:args.start.toString().substring(0,10)
        			},
        			end:{  
        				date:args.end.toString().substring(0,10)
        			},
        			description: description
        		}
        	}).execute(function(event) {
        		location.reload();

        		/*console.log(event);
        	  var start = event.start.date ? event.start.date : event.start.dateTime.split('+')[0];
          	  var end = event.end.date ? event.end.date : event.end.dateTime.split('+')[0];
          	  var e = new RainScheduler.Event({
  			         start: new RainScheduler.Date(start),
  			         end: new RainScheduler.Date(end),
  			         id: event.id,
  			         resource: setResource(event),
  			         text: event.description,
  			         bubbleHtml: event.description,
  			         workerId: event.colorId
  			     });
          	 scheduler.events.add(e);*/
        		
        	})
        }
    };
    
    scheduler.onEventClicked = function(args) {
    	//console.log("arg:"+JSON.stringify(args));
        alert(args.e.text());
    };
    
    scheduler.onTimeHeaderClick = function(args) {
    	//console.log("arg:"+JSON.stringify(args));
        alert(args.header.start);
    };
    
    
    // 구글
    var CLIENT_ID = '194144882467-88aujbou0cbga1qkmkbs39hk8n46j0e4.apps.googleusercontent.com'
    	,DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest","https://www.googleapis.com/discovery/v1/apis/tasks/v1/rest"]
    	,SCOPES = "https://www.googleapis.com/auth/calendar https://www.googleapis.com/auth/tasks"

    // LOAD
    gapi.load('client:auth2', initClient);
    
    function initClient() {
      gapi.client.init({
        discoveryDocs: DISCOVERY_DOCS,
        clientId: CLIENT_ID,
        scope: SCOPES
      }).then(function () {
        gapi.auth2.getAuthInstance().isSignedIn.listen(updateSigninStatus);
        updateSigninStatus(gapi.auth2.getAuthInstance().isSignedIn.get());
      });
    }

    function updateSigninStatus(isSignedIn) {
    	if(isSignedIn){
    		listUpcomingEvents();
    		listUpcomingJobs();
    	}else{
    		gapi.auth2.getAuthInstance().signIn();
    	}
    }

    function handleSignoutClick(event) {
      gapi.auth2.getAuthInstance().signOut();
    }

    function listUpcomingEvents() {
    	
    	// 공휴일
    	var holidays = [];
    	gapi.client.calendar.events.list({
            'calendarId': 'ct171m3icbujnimhtp1j97e47savh8a6@import.calendar.google.com',
            'timeMin': startDate.toISOString(),
            'showDeleted': false,
            'singleEvents': true,
            'maxResults': 20,
            'orderBy': 'startTime'
          }).then(function(response) {
            //console.log(">>>>"+JSON.stringify(response));
            if(response.result.items){
            	$.each(response.result.items, function(i,v){
        			var sdt = new Date(v.start.date), edt = new Date(v.end.date);
        			while(sdt.getTime()<edt.getTime()){
        				holidays.push(sdt.getTime());
        				sdt.setDate(sdt.getDate() + 1);
        			}
            	});
            }
            
            scheduler.onBeforeCellRender = function(args) {
                if (args.cell.start.getDayOfWeek() === 6 || args.cell.start.getDayOfWeek() === 0) {
                    args.cell.backColor = "#f3f3f3";
                    args.cell.cssClass = 'weekend';
                }
                if(holidays.indexOf(args.cell.start.getTime())>-1){
                	 args.cell.backColor = "#f3f3f3";
                     args.cell.cssClass = 'holiday';
                }
            };
            
            
            
            gapi.client.calendar.events.list({
                'calendarId': calId,
                'timeMin': startDate.toISOString(),
                'showDeleted': false,
                'singleEvents': true,
                'maxResults': 100,
                'orderBy': 'startTime'
              }).then(function(response) {
                var events = response.result.items;
                //appendPre('Upcoming events:');

                if (events.length > 0) {
                  for (i = 0; i < events.length; i++) {
                    var event = events[i];
                    var when = event.start.dateTime;
                    if (!when) {
                      when = event.start.date;
                    }
                    //appendPre(event.summary + ' (' + when + ')')
                  }
                  
                  $.each(events, function(i,v){
                	  var start = v.start.date ? v.start.date : v.start.dateTime.split('+')[0];
                	  var end = v.end.date ? v.end.date : v.end.dateTime.split('+')[0];
                	  var e = new RainScheduler.Event({
        			         start: new RainScheduler.Date(start),
        			         end: new RainScheduler.Date(end),
        			         id: v.id,
        			         resource: setResource(v),
        			         text: v.description,
        			         bubbleHtml: v.description,
        			         workerId: v.colorId
        			     });
                	  
                	 scheduler.events.add(e);
                  })
                  
                      scheduler.separators = [
        		        {color:"red",location:(new Date(Date.now() + 1000*60*60*9)).toISOString().substring(0,19)}
        		    ];

                  
                  scheduler.onBeforeEventRender = function(args) {
                	  args.e.cssClass='worker'+args.e.workerId;
                	};
                  
                  
                  scheduler.init();
                  scheduler.scrollTo(new RainScheduler.Date(new Date(Date.now() - 1000*60*60*24*10)));
                  
                } else {
                 // appendPre('No upcoming events found.');
                	scheduler.message('No upcoming events found.');
                }
              });
          });
    }

    function setResource(item){
    	var res = scheduler.resources || [];
    	
    	// [depth1] depth2  ex) [워너원] 디자인
    	var rname = '기타';
    	var name = '이름없음';
    	if(item.summary){
    		rname = item.summary.substring(item.summary.indexOf('[')+1,item.summary.indexOf(']'));
        	rname = !rname?item.summary.substring(item.summary.indexOf('(')+1,item.summary.indexOf(')')):rname;
        	
        	name = item.summary.substring(item.summary.indexOf(']')+1).trim();
    	}
    	
    	var id='', b = false;
    	res.forEach(function(v,i){
    		if(v.name == rname){
    			id = i+'_'+(v.children.length);
    			v.children.push( { name : name, id : id } );
    			b = true;
    			return false;
    		}
    	});
    	if(!b){
    		id = res.length+'_0';
    		res.push({ name: rname, id: res.length+'' , expanded: true, children:[{ name : name, id : id }] } );
    	}
    	
    	scheduler.resources = res;
    	return id;
    }
    
    
    
    
    
    /// JOBS
    var jobs = new RainScheduler.Kanban("jobs");
    jobs.columns.list = [
        {name: "TO DO", id: "T", barColor: "#f9ba25"},
        {name: "ING", id: "I"},
        {name: "COMPLETE", id: "C"}
    ];

    jobs.onBeforeCellRender = function(args) {
        if (args.cell.column.data.id === "T") {
            args.cell.areas =[ {right: 5, bottom: 5, html: "<button> [ + ] </button>", cssClass: "add-button", action: "JavaScript", js: function(args) { add();}} ]
        }
        if (args.cell.column.data.id === "C") {
            args.cell.areas =[ {right: 5, bottom: 5, html: "<button> [ - ] </button>", cssClass: "add-button", action: "JavaScript", js: function(args) { clear();}} ]
        }
    };
    
    jobs.cellMarginBottom = 40;
    
    jobs.columnWidthSpec = "Fixed";
    
    jobs.cardDeleteHandling = "Update";
    
    jobs.onCardDelete = function(args) {
		//console.log(args);
    };
	jobs.onCardDeleted = function(args) {
		gapi.client.tasks.tasks.delete({
    		task : args.card.data.id
    		,tasklist : '@default'
    	}).then(function(response){
    		//console.log(response);
    	});
    };
	
    jobs.onCardMoved = function(args) {
    	//console.log( args.card.data.id );
    	//console.log( args );
    	
    	var task = args.card.data.id, notes = args.card.data.notes||'';
    	switch(args.column.data.id){
			case 'T':
				gapi.client.tasks.tasks.patch({
		    		task : task
		    		,tasklist : '@default'
		    		,resource:{
		    			notes : notes.replace('@ing>','')
		    			,status : 'needsAction'
		    			,completed: null
		    		}
		    	}).then(function(response){
		    		//console.log(response);
		    	});
				break;
			case 'I':
				gapi.client.tasks.tasks.patch({
		    		task : task
		    		,tasklist : '@default'
		    		,resource:{
		    			notes : '@ing>' + notes.replace('@ing>','')
		    			,status : "needsAction"
		    			,completed: null
		    		}
		    	}).then(function(response){
		    		//console.log(response);
		    	});
				break;
			case 'C':
				gapi.client.tasks.tasks.patch({
		    		task : task
		    		,tasklist : '@default'
		    		,resource:{
		    			notes : notes.replace('@ing>','')
		    			,status : 'completed'
		    		}
		    	}).then(function(response){
		    		//console.log(response);
		    	});
				break;
    	}
    	
    	/*gapi.client.tasks.tasks.patch({
    		task : args.card.data.id
    		,resource:{
    			
    		}
    		//,'maxResults' : 100 (default)
    	}).then(function(response){
    		console.log(response);
    		var cards = [];
    		$.each(response.result.items,function(i,v){
    			cards.push({
    				id : v.id
    				,name : v.title||'이름없음'
    				,column : v.status=='needsAction'?(v.notes&&v.notes.indexOf('@:ing')>-1?'I':'T'):'C'
    			});
    		});
    		
    		jobs.cards.list = cards;
    		
    		
    		
    		jobs.init();
    	});*/
    };


    function add() {
        var name = prompt("할일?");
        if (!name) {
            return;
        }
        gapi.client.tasks.tasks.insert({
    		tasklist : '@default'
    		,resource:{
    			title : name
    		}
    	}).then(function(response){
    		//console.log(response);
    		jobs.cards.add({id: response.result.id, name: name, column: "T"});
    	});
    }
    
    function clear() {
    	if( confirm('완료된 할일을 지울까요?') ){
    		gapi.client.tasks.tasks.clear({
    			tasklist : '@default'
    		}).then(function(response){
    			//console.log(response);
    			$.each(jobs.cards.list,function(i,v){
    				if(v && v.column && v.column=='C'){
    					jobs.cards.remove(v);
    				}
    			});
    		});
    	}
    }
    
    function listUpcomingJobs() {
    	gapi.client.tasks.tasklists.list({
    		'maxResults': 10
    	}).then(function(response) {
    		//console.log(response);
    		/*var taskLists = response.result.items;
    		if (taskLists && taskLists.length > 0) {
    			for (var i = 0; i < taskLists.length; i++) {
    				var taskList = taskLists[i];
    				appendPre(taskList.title + ' (' + taskList.id + ')');
    			}
    		} else {
    			appendPre('No task lists found.');
    		}*/
    		
    		/*$.each(response.result.items,function(i,v){
    			if()
    			
            	return false;
    		});*/
    		
    	});
    	
    	gapi.client.tasks.tasks.list({
    		tasklist : '@default'
    		//,showCompleted : false
    		//,'maxResults' : 100 (default)
    	}).then(function(response){
    		//console.log(response);
    		var cards = [];
    		$.each(response.result.items,function(i,v){
    			cards.push({
    				id : v.id
    				,name : v.title||'이름없음'
    				,column : v.status=='needsAction'?(v.notes&&v.notes.indexOf('@ing>')>-1?'I':'T'):'C'
    				,notes : v.notes||''
    			});
    		});
    		
    		jobs.cards.list = cards;
    		
    		
    		
    		jobs.init();
    	});
    	
    }
    
    $(window).keypress(function(e) {
	  	if (e.which == 13) {
			add();
		}
  	});
    
})