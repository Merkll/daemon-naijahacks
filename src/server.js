import express from 'express';
import logger from 'morgan';
import { config } from 'dotenv';
import models, { connect } from './database/models';
import { seedUser, seedContact, seedLocation } from './database/seed';
import router from './routes';

const app = express();

app.use(logger('dev'));
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

app.get('/v1/contacts', async (req, res) => {
  try {
    const contacts = await models.Contact.find({}).populate('user');
    res.json({
      success: true,
      contacts,
    });
  } catch (error) {
    console.log(error);
  }
});

app.get('/v1/locations', async (req, res) => {
  try {
    const locations = await models.Location.find({}).populate('user');
    res.json({
      success: true,
      locations,
    });
  } catch (error) {
    console.log(error);
  }
});

// const eraseDatabaseOnSync = false;
const eraseDatabaseOnSync = true;

connect().then(async () => {
  if (eraseDatabaseOnSync) {
    await Promise.all([
      models.User.deleteMany({}),
      models.Contact.deleteMany({}),
      models.Message.deleteMany({}),
      seedUser(),
      seedContact(),
      seedLocation(),
    ]);
  }
  app.listen(port, () => console.log(`Listening on port ${port}`));
});
