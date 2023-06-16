// Non using Single Responsability Principle

app.post('/users', (req, res) => {
    const user = req.body;

    if (!user || !user.name || !user.email) {
        res.status(400).send('Bad Request!');
    } else {
        pool.query('INSER INTO users (name, email) VALUES (?, ?)',
            [user.name, user.email],
            (error, results) => {
                if (error) {
                    console.error(error);
                    res.status(500).send('Internal error');
                } else {
                    res.status(201).send('User created');
                }
        });
    }
});
