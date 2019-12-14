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

const eraseDatabaseOnSync = false;

connect().then(async () => {
  if (eraseDatabaseOnSync) {
    await Promise.all([
      models.User.deleteMany({}),
      models.Contact.deleteMany({}),
      models.Message.deleteMany({}),
      models.Location.deleteMany({}),
      seedUser(),
      seedContact(),
      seedLocation(),
    ]);
  }
  app.listen(port, () => console.log(`Listening on port ${port}`));
});
