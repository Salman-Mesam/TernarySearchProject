import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException; 


public class CatTree implements Iterable<CatInfo>{
	
	
    public CatNode root;
    
    public CatTree(CatInfo c) {
        this.root = new CatNode(c);
    }
    
    private CatTree(CatNode c) {
        this.root = c;
    }
    
    
    public void addCat(CatInfo c)
    {
        this.root = root.addCat(new CatNode(c));
    }
    
    public void removeCat(CatInfo c)
    {
        this.root = root.removeCat(c);
    }
    
    public int mostSenior()
    {
        return root.mostSenior();
    }
    
    public int fluffiest() {
        return root.fluffiest();
    }
    
    public CatInfo fluffiestFromMonth(int month) {
        return root.fluffiestFromMonth(month);
    }
    
    public int hiredFromMonths(int monthMin, int monthMax) {
        return root.hiredFromMonths(monthMin, monthMax);
    }
    
    public int[] costPlanning(int nbMonths) {
        return root.costPlanning(nbMonths);
    }
    
    
    
    public Iterator<CatInfo> iterator()
    {
        return new CatTreeIterator();
    }
    
    
    class CatNode {
        
        CatInfo data;
        CatNode senior;
        CatNode same;
        CatNode junior;
        
        public CatNode(CatInfo data) {
            this.data = data;
            this.senior = null;
            this.same = null;
            this.junior = null;
        }
        
    public String toString() {
            String result = this.data.toString() + "\n";
            if (this.senior != null) {
                result += "more senior " + this.data.toString() + " :\n";
                result += this.senior.toString();
            }
            if (this.same != null) {
                result += "same seniority " + this.data.toString() + " :\n";
                result += this.same.toString();
            }
            if (this.junior != null) {
                result += "more junior " + this.data.toString() + " :\n";
                result += this.junior.toString();
            }
            return result;
        }
    public CatNode addCat(CatNode c) {
	    //  CatNode root = this 
	   	if(this.data.monthHired> c.data.monthHired) { // c is a senior as smaller no. means was hired earlier
	   		if(this.senior==null)
	   		this.senior=c;
	   		else 
	   		this.senior =this.senior.addCat(c);
	   	}
   		else if (this.data.monthHired<c.data.monthHired) { //cat is a junior of the root  as bigger no. means was hired late
   			if(this.junior==null)
   			this.junior=c;
   			else
   			this.junior=this.junior.addCat(c);
			}
   		else {
   			if (this.data.furThickness >= c.data.furThickness && this.data.monthHired==c.data.monthHired) // MIAW
    		{
    			if(this.same == null)
    				this.same = c;
    			else
    				this.same = this.same.addCat(c);
    		}
   			else // MIAW MIAW
    		{
    			c.junior = this.junior;
    			c.senior = this.senior;
    			c.same = this;
    			this.junior = null;
    			this.senior = null;
    			return c;
    		}
	   	}
	   	return this;
		}

      
        
    public CatNode removeCat(CatInfo c) {  
    	if(this.data.equals(c) && this.same!=null){
    		this.same.senior=this.senior;
    		this.same.junior=this.junior;
    		this.junior=null;
    		this.senior=null;
    		CatNode root = this.same;
    		this.same=null;
    		return root;
    	}
    	
    if(this.data.equals(c) && this.same==null && this.senior!=null ) {
    	if(this.junior==null) {
    		CatNode root = this.senior;
    		this.junior=null;
    		this.senior=null;
    		return root;
  		}
    	CatNode root =this.senior;
    	this.senior=null;
    	root.junior=root.junior.addCat(this.junior);
    	this.junior=null;
    	return root;	
    	
    }
    
   if(this.data.equals(c) && this.same==null && this.senior==null  ) {
	   CatNode root=this.junior;
	   this.senior=null;
	   this.junior=null;
	   return root;
   }
   
   if(!this.data.equals(c) && this.same!=null) {   
    this.same=this.same.removeCat(c);  
   }
   if(!this.data.equals(c) && this.senior!=null) {
	this.senior.removeCat(c);
   }
   if(!this.data.equals(c) && this.junior!=null) {
	this.junior=this.junior.removeCat(c);
   }
   
   if(root==null) {
	  return null;
   }    
    return this;  	
        }

        
          
      public int mostSenior() {
      if(this.senior!=null) {
      int monthHired = this.senior.mostSenior();
      return monthHired;
     }else {
     return this.data.monthHired;
           
     }
            
        }
     
        
       
        public int fluffiest() {   
        	
        	if(this.senior==null && this.same==null && this.junior==null) {  //when I am at the top most root case 1  //this is what I feel I want to add
            	int furthickness=this.data.furThickness;
            	return furthickness;
            }  //dont think they will test this case//too tired to think come back later
  
        	int furthickness =this.data.furThickness;
        	return inordertraverse(this, furthickness);
        	
        }
         private int inordertraverse(CatNode root,int furthickness) {
            	int fluffiest=furthickness;
            	if(root.senior!=null)
            		fluffiest=inordertraverse(root.senior, fluffiest);
            	if(root.junior!=null)
            		fluffiest=inordertraverse(root.junior, fluffiest);
          	if(root.data.furThickness>fluffiest) {
            		fluffiest=root.data.furThickness;
            }
          	
            	return fluffiest;
            	
            }
            
            
            
        
        
        public int hiredFromMonths(int monthMin, int monthMax) {
           if(monthMin>monthMax) {
        	   return 0 ; 	  
           }
           else{
        	
        	CatNode node = this;
            int numberOfCats=0; //Initially counter is at 0
            return traversehiredFromMonths(node,numberOfCats,monthMin,monthMax);
           }   
        }
        
      private int traversehiredFromMonths(CatNode node,int number,int Min ,int Max) {  //Same logic as fluffiest
        	int numberOfCats=number;
    
        	if(node.same!=null)   //traverse when senior not null
        		numberOfCats = traversehiredFromMonths(node.same,numberOfCats,Min,Max);
        	if(node.junior!=null)     //traverse when same not null
        		numberOfCats = traversehiredFromMonths(node.junior,numberOfCats,Min,Max);
            if(node.senior!=null)  //traverse when junior not null
        		numberOfCats = traversehiredFromMonths(node.senior,numberOfCats,Min,Max);
        	
            if(node.data.monthHired >= Min && node.data.monthHired <= Max) {
        		numberOfCats++;  //Increament cat if the criteria met by the visited root
            }
    	    
        
        	return numberOfCats;
        }
        
       
      
      public CatInfo fluffiestFromMonth(int month) {
    	  CatNode node= this;
    	  CatInfo fluffiestDude = node.data ;
    	  
    	  if(fluffiestDude.monthHired!=month) {
    		  
        	  if(node.junior==null && fluffiestDude.monthHired<month )
    			  return null;
        	  if(node.senior==null && fluffiestDude.monthHired>month )
        		  return null;
        	  }
    	  
    	 
    	  while(fluffiestDude.monthHired!=month) {
    		  if(month>node.data.monthHired) 
    			  fluffiestDude=node.senior.data;
    		  if(month<node.data.monthHired)
    			  fluffiestDude=node.junior.data;
    		  if(month==node.data.monthHired)
    			  fluffiestDude=node.data;
    		  
    		  
    	  }
    	  if(fluffiestDude.monthHired==month && node.senior==null && node.junior==null && this.same==null) {
    		  fluffiestDude=node.data;
    		  return fluffiestDude;
    	  }
    	 
    	  
		  
    	  
    	  return fluffiestDude;
      }
     
  
        
        
        
        
        
        public int[] costPlanning(int nbMonths) {
        	  int[] storage = new int[nbMonths];
              CatTree tree =  new CatTree(this);
         
         
           for(CatInfo cat : tree) {
        	  if(cat.nextGroomingAppointment>= 243 && cat.nextGroomingAppointment<=242+nbMonths) {
        		  storage[cat.nextGroomingAppointment-243]+=cat.expectedGroomingCost;
        	  }
        	  
        	   
           }
            return storage;
        }

       

    
       
        
    }
    
    private class CatTreeIterator implements Iterator<CatInfo> {
        // HERE YOU CAN ADD THE FIELDS YOU NEED
    	
        private int index;
        ArrayList<CatInfo> data;
        
        public CatTreeIterator() {
     
        	 data = new ArrayList<CatInfo>();
        	 index=0;
        	 iteratorTraversal(root);
        }
        
        private void iteratorTraversal(CatNode root) {
        	
        	if(root.senior!=null)
        		iteratorTraversal(root.senior);
        	if(root.same!=null) 
          		iteratorTraversal(root.same);  	
        	 data.add(root.data);
        	if(root.junior!=null)
        		iteratorTraversal(root.junior);
   	
        }
        
        public CatInfo next(){
        	if(index<data.size()) {
        		CatInfo c =data.get(index);
        	    index++;
        	    return c;
        	}else {
        		throw new NoSuchElementException("does not exist");
        	}
        		
        	
        }
        
        public boolean hasNext() {
        	if(index<data.size()) {
        		return true;
        	}
            
            return false; // DON'T FORGET TO MODIFY THE RETURN IF NEED BE
        }
    }

 
   }


