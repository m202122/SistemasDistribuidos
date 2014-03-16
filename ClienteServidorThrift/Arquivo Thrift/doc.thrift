namespace cpp tutorial
namespace java tutorial
namespace py tutorial

typedef i32 Int

service Agenda {
	string getDatabase(),
	string insert(1:string name, 2:string fone, 3:Int op),
	string search(1:string name),
	string remove(1:string name)
}
