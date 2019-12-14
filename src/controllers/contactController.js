import Model from '../database/models';

const { Contact } = Model;

const getContacts = async (req, res) => {
  try {
    const contacts = await Contact.find({});
    res.json({
      success: true,
      contacts,
    });
  } catch (error) {
    console.log(error);
  }
};

export default getContacts;
