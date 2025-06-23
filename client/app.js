const { useState, useEffect } = React;

function App() {
  const [items, setItems] = useState([]);
  const [text, setText] = useState('');

  const load = () => {
    fetch('http://localhost:8080/api/todos')
      .then(r => r.json())
      .then(setItems);
  };

  useEffect(load, []);

  const add = () => {
    fetch('http://localhost:8080/api/todos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ description: text })
    }).then(load);
  };

  const complete = id => {
    fetch(`http://localhost:8080/api/todos/${id}/complete`, { method: 'POST' })
      .then(load);
  };

  return (
    <div>
      <h1>Todo List</h1>
      <input value={text} onChange={e => setText(e.target.value)} />
      <button onClick={add}>Add</button>
      <ul>
        {items.map(item => (
          <li key={item.id}>
            {item.description}
            {!item.completedDate && (
              <button onClick={() => complete(item.id)}>Complete</button>
            )}
            {item.completedDate && <span> Completed: {item.completedDate}</span>}
          </li>
        ))}
      </ul>
    </div>
  );
}

ReactDOM.render(<App />, document.getElementById('root'));
