import express from 'express';
import { config } from 'dotenv';
import models, { connect } from './database/models';
import seedDB from './database/seed';
import router from './routes';

const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use('/v1', router);

config();

const port = process.env.PORT || 3000;

app.get('/hello', (req, res) => {
  res.send('Hello world');
});

app.get('/v1/users', async (req, res) => {
  try {
    const users = await models.User.find({});
    res.json({
      success: true,
      users,
    });
  } catch (error) {
    console.log(error);
  }
});
/*
app.post('/v1/register', async (req, res) => {
  const {
    firstName, lastName, phoneNumber, password,
  } = req.body;
  console.log(firstName);
  try {
    const user = await models.User.create({
      firstName,
      lastName,
      phoneNumber,
      password,
    });
    return res.status(201).json({
      message: 'user registered succesfully',
      user,
    });
  } catch (error) {
    console.log(error);

    return res.status(500).json({
      error: 'an error occurred',
    });
  }
}); */

const eraseDatabaseOnSync = true;

connect().then(async () => {
  if (eraseDatabaseOnSync) {
    await Promise.all([
      models.User.deleteMany({}),
      models.Contact.deleteMany({}),
      models.Message.deleteMany({}),
    ]);
    seedDB();
  }
  app.listen(port, () => console.log(`Listening on port ${port}`));
});
