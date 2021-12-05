class ServerService{
    url = '';
    constructor() {
    }
    mainurl(value) {
        this.url = value;
    }

    async getAll(){
        try{
            return (await fetch(this.url)).json();
        }catch (e){
            console.error(e);
        }
    }

    async getById(id){
        try {
            let url = this.url
            return (await fetch(this.url+id)).json();
        }catch (e){
            console.error(e);
        }
    }

    async putData(el){
        try {
            return (await  fetch(this.url,{method: 'PUT',
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                },
                body: JSON.stringify(el)
            }))
        }catch (e){
            console.error(e);
        }
    }

    async saveData(data){
        try{
            return (await fetch(this.url,{
                method:"POST",
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                },
                body: JSON.stringify(data)
            }))
        }catch (e){
            console.error(e);
        }
    }

    async deleteData(id){
        try{
            await fetch(this.url+id,{
                method:"DELETE"
            })
        }catch (e){
            console.error(e);
        }
    }

}